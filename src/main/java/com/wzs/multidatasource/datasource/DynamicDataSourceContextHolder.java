package com.wzs.multidatasource.datasource;

/**
 * @Description: 数据源的切换
 * @Auther: wuzs
 * @Date: 2020/12/18 11:33
 * @Version: 1.0
 */
public class DynamicDataSourceContextHolder {

    /**
     *  使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     *  所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源
     * @param dsType
     */
    public static void setDataSourceType(String dsType) {
        CONTEXT_HOLDER.set(dsType);
    }

    public static String getDataSourceType() {
       return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }
}
