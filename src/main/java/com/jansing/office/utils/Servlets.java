package com.jansing.office.utils;

import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by jansing on 16-11-16.
 */
public class Servlets {
    public static final long ONE_YEAR_SECONDS = 31536000L;
    private static final String[] staticFiles = StringUtil.split(Global.getConfig("web.staticFile"), ",");

    public Servlets() {
    }

    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000L);
        response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
    }

    public static void setNoCacheHeader(HttpServletResponse response) {
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }

    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader("Last-Modified", lastModifiedDate);
    }

    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader("ETag", etag);
    }

    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified) {
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifModifiedSince != -1L && lastModified < ifModifiedSince + 1000L) {
            response.setStatus(304);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(304);
                response.setHeader("ETag", etag);
                return false;
            }
        }

        return true;
    }

    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            String e = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + e + "\"");
        } catch (UnsupportedEncodingException var3) {
            var3.getMessage();
        }
    }

    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Validate.notNull(request, "Request must not be null", new Object[0]);
        Enumeration paramNames = request.getParameterNames();
        TreeMap params = new TreeMap();
        String pre = prefix == null ? "" : prefix;

        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(pre) || paramName.startsWith(pre)) {
                String unprefixed = paramName.substring(pre.length());
                String[] values = request.getParameterValues(paramName);
                if (values != null && values.length != 0) {
                    if (values.length > 1) {
                        params.put(unprefixed, values);
                    } else {
                        params.put(unprefixed, values[0]);
                    }
                } else {
                    values = new String[0];
                }
            }
        }

        return params;
    }

    public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
        StringBuilder queryStringBuilder = new StringBuilder();
        String pre = prefix == null ? "" : prefix;

        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = it.next();
            queryStringBuilder.append(pre).append((String) entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append("&");
            }
        }

        return queryStringBuilder.toString();
    }

    public static String encodeHttpBasic(String userName, String password) {
        String encode = userName + ":" + password;
        return "Basic " + Encodes.encodeBase64(encode.getBytes());
    }

    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception var1) {
            return null;
        }
    }

    /**
     * 假设访问路径
     * http://localhost:8080/image/login?username=aaa&password=ttt
     * 则：
     * getContextPath   /image
     * getServletPath   /login
     * getRequestURI    /image/login
     * getRequestURL    http://localhost:8080/image/login
     * getQueryString   username=aaa&password=ttt
     * 参考org.apache.shiro.web.util.WebUtils.getRequestUri()等
     *
     * @return
     */
    public static String getContextPath() {
        return getRequest().getContextPath();
    }

    public static String getServletPath() {
        return getRequest().getServletPath();
    }

    public static String getRequestURI() {
        return getRequest().getRequestURI();
    }

    public static String getRequestURL() {
        return getRequest().getRequestURL().toString();
    }

    public static String getQueryString() {
        return getRequest().getQueryString();
    }


    public static boolean isStaticFile(String uri) {
        if (staticFiles == null) {
            try {
                throw new Exception("检测到“app.properties”中没有配置“web.staticFile”属性。配置示例：\n#静态文件后缀\nweb.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        return StringUtil.endsWithAny(uri, staticFiles) && !StringUtil.endsWithAny(uri, new CharSequence[]{".jsp", ".java"});
    }
}
