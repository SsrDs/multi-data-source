package com.wzs.multidatasource.annotation;

import com.wzs.multidatasource.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @Description: 自定义数据源注解
 * @Auther: wuzs
 * @Date: 2020/12/18 13:25
 * @Version: 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    /**
     *切换数据源名称
     * @return
     */
    DataSourceType value() default DataSourceType.MASTER;
}
