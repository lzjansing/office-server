package com.jansing.office.service;

import com.jansing.common.persistence.Page;
import com.jansing.common.service.CrudService;
import com.jansing.office.dao.MyFileDao;
import com.jansing.office.entities.MyFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jansing on 16-12-28.
 */
@Service
@Transactional(readOnly = true)
public class MyFileService extends CrudService<MyFileDao, MyFile> {

    public MyFile get(MyFile myFile) {
        return this.dao.get(myFile);
    }

    @Transactional(readOnly = false)
    public void save(MyFile myFile) {
        super.save(myFile);
    }

    @Transactional(readOnly = false)
    public void delete(MyFile myFile) {
        super.delete(myFile);
    }

    @Override
    @Deprecated
    public MyFile get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public List<MyFile> findList(MyFile entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public Page<MyFile> findPage(Page<MyFile> page, MyFile entity) {
        throw new UnsupportedOperationException();
    }
}
