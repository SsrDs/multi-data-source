package com.wzs.multidatasource.service;

import com.wzs.multidatasource.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Auther: wuzs
 * @Date: 2020/12/18 13:40
 * @Version: 1.0
 */
@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public int test1() {
        return testMapper.findCount();
    }

    public int test2() {
        return testMapper.findCount2();
    }

    @Transactional(rollbackFor = Exception.class)
    public int testTra() {
//        int count = testMapper.findCount();
        int count2 = testMapper.findCount2();
        return count2;
    }
}
