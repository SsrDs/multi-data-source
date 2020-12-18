package com.wzs.multidatasource.mapper;

import com.wzs.multidatasource.annotation.DataSource;
import com.wzs.multidatasource.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description:
 * @Auther: wuzs
 * @Date: 2020/12/18 10:02
 * @Version: 1.0
 */
@Mapper
public interface TestMapper {

    @Select("select count(1) from bom")
    int findCount();

    @DataSource(DataSourceType.SALVE)
    @Select("select count(1) from mes_dict")
    int findCount2();
}
