package com.jansing.office.dto;


import com.jansing.common.mapper.JsonMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于兼容owa的数据格式
 * {"SHA256":"gkX+ni0SygDnNlgSfj+kgBlCjdEK9ibUojBzBFVkE5c=","OwnerId":"jansing","Version":"1.0","Size":11775,"BaseFileName":"1.docx"}
 * Created by jansing on 16-12-23.
 */
public class FileInfo {
    private String name;
    private Integer size;
    private String version;
    private String ownerId;
    private String sha256;

    public FileInfo() {
    }

    public FileInfo(Map<String, String> params) {
        this.name = params.get("BaseFileName");
        this.size = Integer.parseInt(params.get("Size"));
        this.version = params.get("Version");
        this.ownerId = params.get("OwnerId");
        this.sha256 = params.get("SHA256");
    }

    public FileInfo(String json) {
        this(JsonMapper.getInstance().fromJson(json, HashMap.class));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }
}
