package com.jansing.office.controller;

import com.google.common.collect.Maps;
import com.jansing.office.converters.LinuxOfficeConverter;
import com.jansing.office.utils.FileUtil;
import com.jansing.office.utils.Global;
import com.jansing.office.utils.HttpClientUtil;
import com.jansing.office.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by jansing on 16-12-23.
 */
@Controller
public class WinOfficeController extends OfficeController{

    private static String owaServerPath = Global.getConfig("convert.win.serverPath");

    @RequestMapping(value = "/winView", method = RequestMethod.GET)
    public String view(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(StringUtil.isBlank(owaServerPath)){
            throw new Exception("请配置OWA服务器地址");
        }

        Map<String, String> params = Maps.newHashMap();
        String owaPagePath = getPagePathAndSetup(req, params);
        FileUtil.copy(httpClientUtil.doGetForInputStream(owaServerPath+owaPagePath, params), resp.getOutputStream());
        resp.flushBuffer();
        resp.setStatus(HttpServletResponse.SC_OK);
        return null;
    }

    public static String getPagePathAndSetup(HttpServletRequest req, Map<String, String> params){
        String fileId = req.getParameter("fileId");
        String remoteContextPath = req.getParameter("host");
        String fileInfoServletPath = remoteContextPath + callbackServletPath + "/" + fileId;
        params.put("WOPISrc", fileInfoServletPath);

        String fileExt = req.getParameter("fileExt");
        if(StringUtil.isBlank(fileExt)){
            throw new IllegalArgumentException("文件格式为空！");
        }
        if(fileExt.equals("doc")||fileExt.equals("docx")){
            return "/wv/wordviewerframe.aspx";
        }
        if(fileExt.equals("xls")||fileExt.equals("xlsx")){
            return "/x/_layouts/xlviewerinternal.aspx";
        }
        if(fileExt.equals("pdf")){
            params.put("PdfMode", "1");
            return "/wv/wordviewerframe.aspx";
        }
        if(fileExt.equals("ppt")||fileExt.equals("pptx")){
            params.put("PowerPointView", "ReadingView");
            return "/p/PowerPointFrame.aspx";
        }
        throw new IllegalArgumentException("不支持该种文件格式！");
    }
}
