package com.jansing.office.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;

import java.io.*;

/**
 * Created by jansing on 16-12-23.
 */
public class FileUtil {
    public static void mkdirsIfNotExisted(String dir){
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        try {
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }

    }

    public static File copy(InputStream is, String dir, String fileName, String sha256) throws Exception {
        FileUtil.mkdirsIfNotExisted(dir);
        File file = new File(dir + File.separator + fileName);
        copy(is, new FileOutputStream(file));

        //check sha256
        if(StringUtil.isNotBlank(sha256)){
            checkSha256(file, sha256);
        }
        return file;
    }

    public static void checkSha256(File file, String sha256) throws Exception {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            String sha256Copy = Base64Utils.encodeToString(DigestUtils.sha256(is));
            if (!sha256Copy.equals(sha256)) {
                throw new Exception("错误，文件不一致！");
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
