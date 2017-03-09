package com.jansing;

import com.google.common.collect.Lists;
import com.jansing.common.utils.Collections3;
import com.jansing.web.utils.FileUtil;
import com.jansing.web.utils.HttpClientUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jansing on 16-11-18.
 */
public class FooTest {


    @Test
    public void test01() {
        StringBuilder sb = new StringBuilder();
        sb.append("123").append("abc");
        System.out.println(sb);
    }

    @Test
    public void test02(){
        List<String> list = Lists.newArrayList();
        list.add("aaa");
        list.add(0, "bbb");
        System.out.println(list);
    }

    @Test
    public void test03() throws IOException {
        System.out.println(LocalDate.now());
    }


    @Test
    public void test04(){
        HttpClientUtil httpClientUtil = new HttpClientUtil(false, null, 600000);
        InputStream is = null;
        String url = "http://202.104.32.195:83/s/base/register/household/!dcc613683b6f9eda6cebc2be355e93b2/household.excel?id=711933&gridid=_1910";
        String filename = "1.xls";
        try {
            is = httpClientUtil.doGetForInputStream(url, null);
            String dir = "/home/jansing/test";
            FileUtil.mkdirsIfNotExisted(dir);
            File file = new File(dir + File.separator + filename);
            OutputStream os = new FileOutputStream(file);
            try {
                IOUtils.copy(is, os);
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Test
    public void test05(){
        List<String> lists = new ArrayList<>();
        lists.add("aaaaaaaaaaaaaa a a a");
        lists.add("!!BWWWW");
        lists.add("wtf");
        System.out.println(Collections3.convertToString(lists, "\n"));
    }

    @Test
    public void test06(){
        System.out.println(new File("/home/jansing").isFile());
        System.out.println(new File("/home/jansing/wen/src/office-server/.gitignore").isFile());
        System.out.println(new File("/home/jansing").isDirectory());
        System.out.println(new File("/home/jansing/wen/src/office-server/.gitignore").isDirectory());
    }

    @Test
    public void test07() throws IOException {
        FileUtil.copy(new FileInputStream("/home/jansing/wen/src/office-server/target/libreoffice-first/convertFile/2017-03-08/f33a55d4c6a041059e2370239eeae22d.pdf"),
                new FileOutputStream("/home/jansing/wen/src/office-server/target/libreoffice-first/convertFile/2017-03-08/f33a55d4c6a041059e2370239eeae22d.pdf"));
    }

    @Test
    public void test08(){
        System.out.println(new File("/home/jansing/wen/src/office-server/target/libreoffice-first/convertFile/2017-03-08/f33a55d4c6a041059e2370239eeae22d.pdf")
            .equals(new File("/home/jansing/wen/src/office-server/target/libreoffice-first/convertFile/2017-03-08//f33a55d4c6a041059e2370239eeae22d.pdf")));
    }

    @Test
    public void test09(){
        System.out.println(FilenameUtils.getExtension("/home/jansing/wen/src/office-server/target/libreoffice-first/convertFile/2017-03-08//f33a55d4c6a041059e2370239eeae22d.pdf"));
    }

    @Test
    public void test10(){
        System.out.println(new File("/home/jansing/wen/src/office-server/.gitignore").length());
        System.out.println(new File("/home/jansing/wen/src/office-server/target/libreoffice-first/convertFile/2017-03-09/1cd23890c8704edfa6d7a26df04163b3.pdf").length());
    }


}
