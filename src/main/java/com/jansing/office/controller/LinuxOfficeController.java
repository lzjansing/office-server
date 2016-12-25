package com.jansing.office.controller;

import com.jansing.office.converters.LinuxOfficeConverter;
import com.jansing.office.dto.FileInfo;
import com.jansing.office.utils.FileUtil;
import com.jansing.office.utils.Global;
import org.apache.commons.io.IOUtils;
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
public class LinuxOfficeController extends OfficeController{

    @Override
    @RequestMapping(value = "/linuxView", method = RequestMethod.GET)
    public String view(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        //convert the file
        String convertPath = req.getSession().getServletContext().getRealPath(Global.getConvertPath())
                + File.separator + LocalDate.now().toString();
        File pdfFile = LinuxOfficeConverter.convertToPdf(getFile(req, convertPath), convertPath);
        String realPath = pdfFile.getAbsolutePath();
        model.addAttribute("file", realPath.substring(realPath.indexOf(Global.getConvertPath())));
        return "/viewer";
    }
    /**
     * 从文件服务器获取文件
     * @param req
     * @return
     * @throws Exception
     */
    public File getFile(HttpServletRequest req, String savePath) throws Exception {
        String fileId = req.getParameter("fileId");
        String remoteContextPath = req.getParameter("host");

        InputStream is = null;
        try {
            //get the file info
            String fileInfoServletPath = remoteContextPath + callbackServletPath + "/" + fileId;
//        {"SHA256":"gkX+ni0SygDnNlgSfj+kgBlCjdEK9ibUojBzBFVkE5c=","OwnerId":"jansing","Version":"1.0","Size":11775,"BaseFileName":"1.docx"}
            FileInfo fileInfo = new FileInfo(httpClientUtil.doGet(fileInfoServletPath, null));

            //get the file
            String fileServletPath = remoteContextPath + callbackServletPath + "/" + fileId + "/contents";
            is = httpClientUtil.doGetForInputStream(fileServletPath, null);
            return FileUtil.copy(is, savePath, fileInfo.getName(), fileInfo.getSha256());
        } catch (IOException e) {
            logger.error("不明错误？？"+e.getMessage());
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }


}
