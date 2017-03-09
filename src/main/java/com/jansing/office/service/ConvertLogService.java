package com.jansing.office.service;

import com.jansing.common.persistence.Page;
import com.jansing.common.service.CrudService;
import com.jansing.office.dao.ConvertLogDao;
import com.jansing.office.entities.ConvertLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jansing on 16-12-29.
 */
@Service
@Transactional(readOnly = true)
public class ConvertLogService extends CrudService<ConvertLogDao, ConvertLog> {

    @Override
    @Transactional(readOnly = false)
    public void save(ConvertLog entity) {
        super.save(entity);
    }

    @Override
    @Deprecated
    public ConvertLog get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public List<ConvertLog> findList(ConvertLog entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<ConvertLog> findPage(Page<ConvertLog> page, ConvertLog entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void delete(ConvertLog entity) {
        throw new UnsupportedOperationException();
    }
}
