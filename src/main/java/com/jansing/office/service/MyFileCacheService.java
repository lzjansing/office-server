package com.jansing.office.service;

import com.jansing.common.persistence.Page;
import com.jansing.common.service.CrudService;
import com.jansing.office.dao.MyFileCacheDao;
import com.jansing.office.entities.MyFileCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jansing on 16-12-28.
 */
@Service
@Transactional(readOnly = true)
public class MyFileCacheService extends CrudService<MyFileCacheDao, MyFileCache> {

    public MyFileCache get(MyFileCache myFileCache) {
        return this.dao.get(myFileCache);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(MyFileCache entity) {
        super.save(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(MyFileCache entity) {
        super.delete(entity);
    }

    @Override
    @Deprecated
    public MyFileCache get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public List<MyFileCache> findList(MyFileCache entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public Page<MyFileCache> findPage(Page<MyFileCache> page, MyFileCache entity) {
        throw new UnsupportedOperationException();
    }
}
