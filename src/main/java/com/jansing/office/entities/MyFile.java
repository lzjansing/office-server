package com.jansing.office.entities;

import com.jansing.common.mapper.JsonMapper;
import com.jansing.common.persistence.DataEntity;
import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jansing on 16-12-28.
 */
public class MyFile extends DataEntity<MyFile> {
    private String name;
    private Integer size;
    private String version;
    private String ownerId;
    private String sha256;

    private String ext;
    private String characterSet;
    private String md5;
    private String appId;
    private String appFileId;


    public MyFile() {
    }

    public MyFile(Map<String, Object> params) {
        String name = (String) params.get("BaseFileName");
        this.name = FilenameUtils.getBaseName(name);
        this.ext = FilenameUtils.getExtension(name);
        this.size = (Integer) params.get("Size");
        this.version = (String) params.get("Version");
        this.ownerId = (String) params.get("OwnerId");
        this.sha256 = (String) params.get("SHA256");
    }

    public MyFile(String json) {
        this(JsonMapper.getInstance().fromJson(json, HashMap.class));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getAbsoluteName() {
        return getName() + "." + getExt();
    }

    public void setAbsoluteName(String absoluteName) {
        setName(FilenameUtils.getBaseName(absoluteName));
        setExt(FilenameUtils.getExtension(absoluteName));
    }

    public String getRealFileName() {
        return getId() + "." + getExt();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
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

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppFileId() {
        return appFileId;
    }

    public void setAppFileId(String appFileId) {
        this.appFileId = appFileId;
    }
}
