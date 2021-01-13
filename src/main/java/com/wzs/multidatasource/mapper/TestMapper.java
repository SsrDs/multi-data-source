package com.wzs.multidatasource.mapper;

import org.apache.ibatis.annotations.Insert;
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

    @Select("select count(1) from user")
    int findCount();

    @Insert("insert into user (name) value ('wgs')")
    int insertUser();
}
