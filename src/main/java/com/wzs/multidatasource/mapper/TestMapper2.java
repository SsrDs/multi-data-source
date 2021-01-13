package com.wzs.multidatasource.mapper;

import com.wzs.multidatasource.annotation.DataSource;
import com.wzs.multidatasource.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper2 {
    @DataSource(DataSourceType.SALVE)
    int findCount2();

    @DataSource(DataSourceType.SALVE)
    int insertUser2();

    void create();
}
