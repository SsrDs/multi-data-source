package com.wzs.multidatasource.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Auther: wuzs
 * @Date: 2020/12/18 13:47
 * @Version: 1.0
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor {
    private static ConfigurableListableBeanFactory beanFactory;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        this.beanFactory = configurableListableBeanFactory;
    }

    @SuppressWarnings("unchecked")
    public static<T> T getBean(String beanName) {
        return (T)beanFactory.getBean(beanName);
    }
}
