package com.wzs.multidatasource.service;

import com.wzs.multidatasource.mapper.TestMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService2 {
    @Autowired
    private TestMapper2 testMapper2;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void create() {
        testMapper2.create();
    }
}
