package com.jansing.office.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by jansing on 16-11-4.
 */
public class Global {
    private static Global global = new Global();
    private static Map<String, String> map = Maps.newHashMap();
    private static PropertiesLoader loader = new PropertiesLoader(new String[]{"custom.properties"});
    public static final String SHOW = "1";
    public static final String HIDE = "0";
    public static final String YES = "1";
    public static final String NO = "0";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public Global() {
    }

    public static Global getInstance() {
        return global;
    }

    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = loader.getProperty(key);
            map.put(key, value != null ? value : "");
        }

        return value;
    }

    public static String getConvertPath() {
        return getConfig("convertPath");
    }

    public static Boolean isDemoMode() {
        String dm = getConfig("demoMode");
        return Boolean.valueOf("true".equalsIgnoreCase(dm) || "1".equals(dm));
    }

    public static Object getConst(String field) {
        try {
            return Global.class.getField(field).get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getConst(String obj, String field) {
        try {
            Class cla = Class.forName(obj);
            return cla.getField(field).get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getProjectPath() {
        String projectPath = getConfig("projectPath");
        if (StringUtils.isNotBlank(projectPath)) {
            return projectPath;
        } else {
            try {
                File e = (new DefaultResourceLoader()).getResource("").getFile();
                if (e != null) {
                    while (true) {
                        System.out.println(e.getAbsolutePath());
                        File f = new File(e.getPath() + File.separator + "src" + File.separator + "main");
                        if (f == null || f.exists() || e.getParentFile() == null) {
                            projectPath = e.toString();
                            break;
                        }

                        e = e.getParentFile();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return projectPath;
        }
    }
}
