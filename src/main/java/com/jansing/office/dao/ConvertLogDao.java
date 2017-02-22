package com.jansing.office.dao;

import com.jansing.common.persistence.CrudDao;
import com.jansing.common.persistence.annotation.MyBatisDao;
import com.jansing.office.entities.ConvertLog;

/**
 * Created by jansing on 16-12-28.
 */
@MyBatisDao
public interface ConvertLogDao extends CrudDao<ConvertLog> {
}
