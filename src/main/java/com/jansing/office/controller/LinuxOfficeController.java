package com.jansing.office.controller;

import com.jansing.common.config.Global;
import com.jansing.office.converters.LinuxOfficeConverter;
import com.jansing.office.entities.ConvertLog;
import com.jansing.office.entities.MyFile;
import com.jansing.office.entities.MyFileCache;
import com.jansing.office.service.MyFileCacheService;
import com.jansing.office.service.MyFileService;
import com.jansing.web.utils.FileTransmitUtil;
import com.jansing.web.utils.FileUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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

    private static final String CONVERT_PATH = Global.getConfig("convertPath");

    @Override
    @RequestMapping(value = "/linuxView", method = RequestMethod.GET)
    public String view(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ConvertLog convertLog = (ConvertLog) req.getAttribute("convertLog");
        String fileId = req.getParameter("fileId");
        //host = "http://127.0.0.1:8080/swopi/"
        String remoteContextPath = req.getParameter("host");
        String fileInfoUrl = remoteContextPath + callbackServletPath + "/" + fileId;
        String fileUrl = fileInfoUrl + "/contents";
        String savePath = FileTransmitUtil.getRealPath(req, CONVERT_PATH)
                + File.separator + LocalDate.now().toString();

        MyFile myFile = new MyFile(httpClientUtil.doGet(fileInfoUrl, null));
        MyFileCache cache = ensureOriginExits(req, myFile, fileUrl, savePath);
        long convertStart = System.currentTimeMillis();
        ensureConvertExits(req, cache, savePath);
        long convertEnd = System.currentTimeMillis();

        if (cache.getId() == null || !cache.getConvertPath().equals(myFileCacheService.get(cache).getConvertPath())) {
            myFileCacheService.save(cache);
            convertLog.setConvertCost(convertEnd - convertStart);
        }

        model.addAttribute("fileName", myFile.getName());
        model.addAttribute("file", cache.getConvertPath());
        if (LinuxOfficeConverter.isXls(myFile.getExt())) {
            return "xlsViewer";
        }
        return "/viewer";
    }

    public MyFileCache ensureOriginExits(HttpServletRequest req, MyFile myFile, String fileUrl, String savePath) throws Exception {
        MyFileCache cache = new MyFileCache();
        MyFile old = myFileService.get(myFile);
        if (old != null && old.getId() != null) {
            cache.setMyFile(old);
            MyFileCache tmpCache = myFileCacheService.get(cache);
            if (tmpCache != null) {
                if (FileTransmitUtil.askFileExist(tmpCache.getOriginPath(), req)) {
                    return tmpCache;
                }
                cache = tmpCache;
            }
            myFile = old;
        } else {
            myFileService.save(myFile);
            myFile = myFileService.get(myFile);
            cache.setMyFile(myFile);
        }
        cache.setOriginPath(FileTransmitUtil.getRelativePath(req,
                getFile(myFile, fileUrl, savePath)));
        return cache;
    }

    /**
     * @param fileUrl
     * @param savePath
     * @param myFile
     * @return 获取到的文件的绝对路径
     * @throws Exception
     */
    public String getFile(MyFile myFile, String fileUrl, String savePath) throws Exception {
        InputStream is = null;
        try {
            is = httpClientUtil.doGetForInputStream(fileUrl, null);
            File file = FileUtil.copy(is, savePath, myFile.getRealFileName(), myFile.getSha256());
            return file.getAbsolutePath();
        } catch (IOException e) {
            logger.error("不明错误？？" + e.getMessage());
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public void ensureConvertExits(HttpServletRequest req, MyFileCache cache, String savePath) throws Exception {
        if (!FileTransmitUtil.askFileExist(cache.getConvertPath(), req)) {
            cache.setConvertPath(FileTransmitUtil.getRelativePath(req,
                    convertFile(FileTransmitUtil.getRealPath(req, cache.getOriginPath()), savePath)));
        }
    }

    /**
     * @param sourcePath  源文件绝对路径
     * @param convertPath 转换后的文件的绝对路径，不含文件名
     * @return 转换后的文件的绝对路径，或url(暂不支持)
     * @throws Exception
     */
    public String convertFile(String sourcePath, String convertPath) throws Exception {
        return LinuxOfficeConverter.convertToPdf(new File(sourcePath), convertPath)
                .getAbsolutePath();
    }
}
