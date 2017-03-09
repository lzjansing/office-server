package com.jansing.office.controller;

import com.google.common.collect.Maps;
import com.jansing.common.config.Global;
import com.jansing.web.utils.FileUtil;
import com.jansing.web.utils.StringUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by jansing on 16-12-23.
 */
@Controller
public class WinOfficeController extends OfficeController {

    private static String owaServerPath = Global.getConfig("convert.win.serverPath");

    @RequestMapping(value = "/winView", method = RequestMethod.GET)
    public String view(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (StringUtil.isBlank(owaServerPath)) {
            throw new Exception("请配置OWA服务器地址");
        }

        Map<String, String> params = Maps.newHashMap();
        String owaPagePath = getPagePathAndSetup(req, params);
        InputStream in = httpClientUtil.doGetForInputStream(owaServerPath + owaPagePath, params);
        OutputStream out = resp.getOutputStream();
        try {
            FileUtil.copy(in, out);
            resp.flushBuffer();
            resp.setStatus(HttpServletResponse.SC_OK);
            return null;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    public static String getPagePathAndSetup(HttpServletRequest req, Map<String, String> params) {
        String fileId = req.getParameter("fileId");
        String remoteContextPath = req.getParameter("host");
        String fileInfoServletPath = remoteContextPath + callbackServletPath + "/" + fileId;
        params.put("WOPISrc", fileInfoServletPath);

        String fileExt = req.getParameter("fileExt");
        if (StringUtil.isBlank(fileExt)) {
            throw new IllegalArgumentException("文件格式为空！");
        }
        if (fileExt.equals("doc") || fileExt.equals("docx")) {
            return "/wv/wordviewerframe.aspx";
        }
        if (fileExt.equals("xls") || fileExt.equals("xlsx")) {
            return "/x/_layouts/xlviewerinternal.aspx";
        }
        if (fileExt.equals("pdf")) {
            params.put("PdfMode", "1");
            return "/wv/wordviewerframe.aspx";
        }
        if (fileExt.equals("ppt") || fileExt.equals("pptx")) {
            params.put("PowerPointView", "ReadingView");
            return "/p/PowerPointFrame.aspx";
        }
        throw new IllegalArgumentException("不支持该种文件格式！");
    }
}
