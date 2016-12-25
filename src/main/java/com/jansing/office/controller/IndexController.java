package com.jansing.office.controller;

import com.google.common.collect.Lists;
import com.jansing.office.utils.Global;
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
    @Autowired
    private WinOfficeController winOfficeController;
    @Autowired
    private LinuxOfficeController linuxOfficeController;
    private List<OfficeController> serverList;

    private Boolean winFirst = Boolean.valueOf(Global.getConfig("convert.win.first"));
    private Boolean winOn = Boolean.valueOf(Global.getConfig("convert.win.on"));
    private Boolean linuxOn = Boolean.valueOf(Global.getConfig("convert.linux.on"));

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
        if (serverList == null) {
            initServerList();
        }
        if (serverList.size() == 0) {
            logger.error("没有找到提供转换服务的服务器！请检查配置");
        }
        //todo 怎么实现如果失败就尝试serverList的下一个？
        return serverList.get(0).view(model, req, resp);
    }
}
