package com.jansing.office.controller;

import com.jansing.common.config.Global;
import com.jansing.web.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jansing on 16-12-24.
 */
public abstract class OfficeController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected HttpClientUtil httpClientUtil
            = new HttpClientUtil(false, null, Integer.parseInt(Global.getConfig("httpclient.timeout")));
    //取文件的回调servletPath
    protected static String callbackServletPath = "/wopi/files";

    public abstract String view(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
