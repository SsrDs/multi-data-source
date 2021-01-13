package com.wzs.multidatasource.service;

import com.wzs.multidatasource.mapper.TestMapper;
import com.wzs.multidatasource.mapper.TestMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Autowired
    private TestMapper2 testMappe2;

    @Autowired
    private TestService2 testService2;

    public int test1() {
        return testMapper.findCount();
    }

    public int test2() {
        return testMappe2.findCount2();
    }

    @Transactional(rollbackFor = Exception.class)
    public int testTra() {
        testMapper.insertUser();
        testMappe2.insertUser2();
        int count = testMapper.findCount();
        int count2 = testMappe2.findCount2();
        testService2.create();
        System.out.println(1/0);
        return count2 + count;
    }
}
