package com.jansing.office.entities;

import com.jansing.common.persistence.DataEntity;

/**
 * Created by jansing on 16-12-28.
 */
public class ConvertLog extends DataEntity<ConvertLog> {
    private MyFile myFile;
    private Long cost;
    private Boolean useCache = false;
    private Long convertCost;

    public MyFile getMyFile() {
        return myFile;
    }

    public void setMyFile(MyFile myFile) {
        this.myFile = myFile;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Boolean getUseCache() {
        return useCache;
    }

    public void setUseCache(Boolean useCache) {
        this.useCache = useCache;
    }

    public Long getConvertCost() {
        return convertCost;
    }

    public void setConvertCost(Long convertCost) {
        this.convertCost = convertCost;
    }
}
