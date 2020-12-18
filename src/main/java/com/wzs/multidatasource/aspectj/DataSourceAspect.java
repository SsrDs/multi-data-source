package com.wzs.multidatasource.aspectj;

import com.mysql.cj.util.StringUtils;
import com.wzs.multidatasource.annotation.DataSource;
import com.wzs.multidatasource.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description:
 * @Auther: wuzs
 * @Date: 2020/12/18 13:19
 * @Version: 1.0
 */
@Aspect
@Component
@Order(-1)
public class DataSourceAspect {

    @Pointcut("@annotation(com.wzs.multidatasource.annotation.DataSource) " +
            "|| @within(com.wzs.multidatasource.annotation.DataSource)")
    public void setPointCut(){}

    @Around("setPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);
        if (dataSource != null) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }
        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
