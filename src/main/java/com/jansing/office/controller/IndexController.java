package com.jansing.office.controller;

import com.google.common.collect.Lists;
import com.jansing.common.config.Global;
import com.jansing.office.entities.ConvertLog;
import com.jansing.office.service.ConvertLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by jansing on 16-12-17.
 */
@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    public static final String OS_LINUX = "linux";
    public static final String OS_WIN = "win";
    @Autowired
    private WinOfficeController winOfficeController;
    @Autowired
    private LinuxOfficeController linuxOfficeController;
    @Autowired
    private ConvertLogService convertLogService;
    private List<OfficeController> serverList;

    private Boolean winFirst = Boolean.valueOf(Global.getConfig("convert.win.first"));
    protected Boolean winOn = Boolean.valueOf(Global.getConfig("convert.win.on"));
    protected Boolean linuxOn = Boolean.valueOf(Global.getConfig("convert.linux.on"));

    private void initServerList() {
        serverList = Lists.newArrayList();
        if (Boolean.TRUE.equals(winOn)) {
            serverList.add(winOfficeController);
        }
        if (Boolean.TRUE.equals(linuxOn)) {
            if (winFirst == null || Boolean.TRUE.equals(winFirst)) {
                serverList.add(linuxOfficeController);
            } else {
                serverList.add(0, linuxOfficeController);
            }
        }
    }

    /**
     * @param model
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ConvertLog convertLog = new ConvertLog();
        long start = System.currentTimeMillis();
        req.setAttribute("convertLog", convertLog);
        String os = req.getParameter("os");
        String viewer = os == null ? commonView(model, req, resp) : specialView(os, model, req, resp);
        convertLog.setCost(System.currentTimeMillis() - start);
        convertLogService.save(convertLog);
        return viewer;
    }

    private String commonView(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (serverList == null) {
            initServerList();
        }
        if (serverList.size() == 0) {
            logger.error("没有找到提供转换服务的服务器！请检查配置");
            throw new Exception("没有找到提供转换服务的服务器！");
        }
        return serverList.get(0).view(model, req, resp);
    }

    private String specialView(String os, Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (OS_WIN.equals(os)) {
            if (winOn) {
                return winOfficeController.view(model, req, resp);
            } else {
                logger.warn("不支持win操作系统的转换平台，使用默认平台转换");
                return commonView(model, req, resp);
            }
        } else if (OS_LINUX.equals(os)) {
            if (linuxOn) {
                return linuxOfficeController.view(model, req, resp);
            } else {
                logger.warn("不支持linux操作系统的转换平台，使用默认平台转换");
                return commonView(model, req, resp);
            }
        }
        logger.warn("未知操作系统的转换平台，使用默认平台转换");
        return serverList.get(0).view(model, req, resp);
    }


}
