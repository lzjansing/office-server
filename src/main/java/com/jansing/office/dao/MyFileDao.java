package com.jansing.office.dao;

import com.jansing.common.persistence.CrudDao;
import com.jansing.common.persistence.annotation.MyBatisDao;
import com.jansing.office.entities.MyFile;

/**
 * Created by jansing on 16-12-28.
 */
@MyBatisDao
public interface MyFileDao extends CrudDao<MyFile> {
//    T get(String var1);
//    List<T> findList(T var1);
//    int update(T var1);
}
