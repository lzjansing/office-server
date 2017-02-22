package com.jansing.office.controller;

import com.jansing.common.config.Global;
import com.jansing.office.converters.LinuxOfficeConverter;
import com.jansing.office.entities.ConvertLog;
import com.jansing.office.entities.MyFile;
import com.jansing.office.entities.MyFileCache;
import com.jansing.office.service.MyFileCacheService;
import com.jansing.office.service.MyFileService;
import com.jansing.office.utils.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Created by jansing on 16-12-24.
 */
@Controller
public class LinuxOfficeController extends OfficeController {

    @Autowired
    private MyFileService myFileService;
    @Autowired
    private MyFileCacheService myFileCacheService;

    @Override
    @RequestMapping(value = "/linuxView", method = RequestMethod.GET)
    public String view(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ConvertLog convertLog = (ConvertLog) req.getAttribute("convertLog");
        String fileId = req.getParameter("fileId");
        //文件所在服务器路径
        String remoteContextPath = req.getParameter("host");
        String fileInfoUrl = remoteContextPath + callbackServletPath + "/" + fileId;
        String fileUrl = fileInfoUrl + "/contents";
        //保存文件的路径
        String convertPath = req.getSession().getServletContext().getRealPath(Global.getConfig("convertPath"))
                + File.separator + LocalDate.now().toString();

        MyFile myFile = new MyFile(httpClientUtil.doGet(fileInfoUrl, null));
        //检查是否已存在，即曾经请求过这个文件
        MyFileCache cache = new MyFileCache();
        MyFile old = myFileService.get(myFile);
        if (old != null && old.getId() != null) {
            cache.setMyFile(old);
            convertLog.setMyFile(old);
            MyFileCache tmpCache = myFileCacheService.get(cache);
            if (tmpCache != null) {
                if (tmpCache.getConvertPath() != null && new File(tmpCache.getConvertPath()).exists()) {
                    convertLog.setUseCache(true);
                    model.addAttribute("file", tmpCache.getConvertPath().substring(tmpCache.getConvertPath().indexOf(Global.getConfig("convertPath"))));
                    return "/viewer";
                }
                tmpCache.setConvertPath(null);
                if (tmpCache.getOriginPath() != null && new File(tmpCache.getOriginPath()).exists()) {
                    long start = System.currentTimeMillis();
                    convertFile(tmpCache, convertPath);
                    convertLog.setConvertCost(System.currentTimeMillis() - start);
                    myFileCacheService.save(tmpCache);
                    model.addAttribute("file", tmpCache.getConvertPath().substring(tmpCache.getConvertPath().indexOf(Global.getConfig("convertPath"))));
                    return "/viewer";
                }
                tmpCache.setOriginPath(null);
                cache = tmpCache;
            }
            cache.setMyFile(old);
        } else {
            myFileService.save(myFile);
            cache.setMyFile(myFile);
            convertLog.setMyFile(myFile);
        }
        //confirm the file exist
        getFile(fileUrl, convertPath, cache);
        //confirm the convert exist
        long start = System.currentTimeMillis();
        convertFile(cache, convertPath);
        convertLog.setConvertCost(System.currentTimeMillis() - start);
        myFileCacheService.save(cache);

        model.addAttribute("file", cache.getConvertPath().substring(cache.getConvertPath().indexOf(Global.getConfig("convertPath"))));
        return "/viewer";
    }

    public void convertFile(MyFileCache cache, String convertPath) throws Exception {
        if (cache.getOriginPath() == null || !new File(cache.getOriginPath()).exists()) {
            throw new FileNotFoundException("转换失败，要转换的文件不存在！");
        }
        if ("pdf".equals(FilenameUtils.getExtension(cache.getOriginPath()))) {
//            本身就是pdf文档，无需转换
            cache.setConvertPath(cache.getOriginPath());
        } else {
            File pdfFile = LinuxOfficeConverter.convertToPdf(new File(cache.getOriginPath()), convertPath);
            cache.setConvertPath(pdfFile.getAbsolutePath());
        }
    }


    public void getFile(String url, String savePath, MyFileCache cache) throws Exception {
        InputStream is = null;
        try {
            is = httpClientUtil.doGetForInputStream(url, null);
            File file = FileUtil.copy(is, savePath, cache.getMyFile().getRealFileName(), cache.getMyFile().getSha256());
            cache.setOriginPath(file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("不明错误？？" + e.getMessage());
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }


}
