package com.jansing.office.entities;

import com.jansing.common.persistence.DataEntity;

/**
 * Created by jansing on 16-12-28.
 */
public class MyFileCache extends DataEntity<MyFileCache> implements Cloneable {
    private MyFile myFile;
    private String originPath;
    private String convertPath;
    private String servletPath;

    public MyFile getMyFile() {
        return myFile;
    }

    public void setMyFile(MyFile myFile) {
        this.myFile = myFile;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public String getConvertPath() {
        return convertPath;
    }

    public void setConvertPath(String convertPath) {
        this.convertPath = convertPath;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    @Override
    protected MyFileCache clone() throws CloneNotSupportedException {
        MyFileCache cache = (MyFileCache) super.clone();
        cache.setMyFile(myFile.clone());
        return cache;
    }
}
