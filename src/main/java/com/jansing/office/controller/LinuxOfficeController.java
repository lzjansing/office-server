package com.jansing.office.controller;

import com.jansing.common.config.Global;
import com.jansing.common.mapper.JsonMapper;
import com.jansing.common.utils.Message;
import com.jansing.common.utils.StringUtil;
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

    private final String CONVERT_PATH = Global.getConfig("convertPath");
    private final String saveServerPath = Global.getConfig("convert.saveServer.path");
    private final String saveServerUploadPath = saveServerPath + Global.getConfig("convert.saveServer.upload");
    private final String saveServerAskExistPath = saveServerPath + Global.getConfig("convert.saveServer.askExist");

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
        MyFileCache cache = loadCache(myFile);
        if (StringUtil.isBlank(cache.getConvertPath()) || !askFileExist(cache.getConvertPath(), req)) {
            cache = ensureOriginExist(req, cache, fileUrl, savePath);
            long convertStart = System.currentTimeMillis();
            ensureConvertExist(req, cache, savePath);
            long convertEnd = System.currentTimeMillis();

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

    /**
     * cache.myFile需包含完整信息
     * 如果cache不存在则new
     *
     * @param myFile
     * @return
     * @throws Exception
     */
    public MyFileCache loadCache(MyFile myFile) throws Exception {
        MyFileCache cache = new MyFileCache();
        MyFile old = myFileService.get(myFile);
        if (old != null && old.getId() != null) {
            cache.setMyFile(old);
            MyFileCache tmpCache = myFileCacheService.get(cache);
            if (tmpCache != null) {
                tmpCache.setMyFile(old);
                return tmpCache;
            }
        } else {
            myFileService.save(myFile);
            myFile = myFileService.get(myFile);
            cache.setMyFile(myFile);
        }
        return cache;
    }

    public MyFileCache ensureOriginExist(HttpServletRequest req, MyFileCache cache, String fileUrl, String savePath) throws Exception {
        MyFile myFile = cache.getMyFile();
        if (!askFileExist(cache.getOriginPath(), req)) {
            cache.setOriginPath(FileTransmitUtil.getRelativePath(req,
                    getFile(myFile, fileUrl, savePath).getAbsolutePath()));
        }
        return cache;
    }

    /**
     * @param fileUrl
     * @param savePath
     * @param myFile
     * @return
     * @throws Exception
     */
    public File getFile(MyFile myFile, String fileUrl, String savePath) throws Exception {
        InputStream is = null;
        try {
            is = httpClientUtil.doGetForInputStream(fileUrl, null);
            File file = FileUtil.copy(is, savePath, myFile.getRealFileName(), myFile.getSha256());
            return file;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public void ensureConvertExist(HttpServletRequest req, MyFileCache cache, String savePath) throws Exception {
        File file = convertFile(FileTransmitUtil.getRealPath(req, cache.getOriginPath()), savePath);
        String filePath = null;
        try {
            filePath = postFile(file);
        } catch (Exception e) {
            logger.warn(e.getMessage() + " ->存放本地服务器! ");
            filePath = file.getAbsolutePath();
        }
        cache.setConvertPath(FileTransmitUtil.getRelativePath(req, filePath));
    }

    /**
     * @param sourcePath  源文件绝对路径
     * @param convertPath 转换后的文件的绝对路径，不含文件名
     * @return
     * @throws Exception
     */
    public File convertFile(String sourcePath, String convertPath) throws Exception {
        return LinuxOfficeConverter.convertToPdf(new File(sourcePath), convertPath);
    }

    private boolean askFileExist(String filePath, HttpServletRequest req) throws Exception {
        return FileTransmitUtil.askFileExist(filePath, saveServerAskExistPath, req);
    }

    private String postFile(File file) throws Exception {
        Message message = null;
        if (file != null && file.exists()) {
            //尝试把文件发送到文件服务器中
            try {
                String json = httpClientUtil.doPostFile(saveServerUploadPath, "file", file);
                message = (Message) JsonMapper.fromJsonString(json, Message.class);
                if (Message.SUCCESS.equals(message.getCode())) {
                    return String.valueOf(message.getExtra().get(FileTransmitUtil.PATH_KEY));
                }
            } catch (Exception e) {
                logger.error("文件服务器挂了「" + e.getMessage() + "」");
                throw new Exception("文件服务器挂了「" + e.getMessage() + "」");
            }
        } else {
            message.setCode(Message.FAIL);
            message.setMessage("文件不存在！");
        }
        throw new Exception("Error : 「" + message.getMessage() + "」");
    }
}
