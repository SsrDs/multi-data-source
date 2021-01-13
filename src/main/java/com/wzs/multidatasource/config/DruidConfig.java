package com.wzs.multidatasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.wzs.multidatasource.config.properties.DruidProperties;
import com.wzs.multidatasource.datasource.DynamicDataSource;
import com.wzs.multidatasource.enums.DataSourceType;
import com.wzs.multidatasource.utils.SpringUtils;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: druid配置多数据源
 * @Auther: wuzs
 * @Date: 2020/12/18 10:53
 * @Version: 1.0
 */
@Configuration
@MapperScan(basePackages = "com.wzs.multidatasource.mapper", sqlSessionTemplateRef  = "sqlSessionTemplate")
public class DruidConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
//        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return new DruidXADataSource();
    }

    @Bean("salveDataSource")
    @ConfigurationProperties("spring.datasource.druid.salve")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.salve",name = "enabled", havingValue = "true")
    public DataSource salveDataSource(DruidProperties druidProperties) {
        return new DruidXADataSource();
//        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
//        return druidProperties.dataSource(dataSource);
    }

//    @Bean(name = "dynamicDataSource")
//    @Primary
//    public DynamicDataSource dataSource(DataSource masterDataSource) {
//        HashMap<Object, Object> targetDataSource = new HashMap<>();
//        targetDataSource.put(DataSourceType.MASTER.name(),masterDataSource);
//        setDataSource(targetDataSource,DataSourceType.SALVE.name(),"salveDataSource");
//        return new DynamicDataSource(masterDataSource,targetDataSource);
//    }

    //targetDataSource为备选数据源集合
    public void setDataSource(Map<Object,Object> targetDataSource,String sourceName, String beanName) {
        DataSource dataSource = SpringUtils.getBean(beanName);
        targetDataSource.put(sourceName,dataSource);
    }

    /**
     * 创建支持 XA 事务的 Atomikos 数据源 mydbone
     * @param masterDataSource
     * @return
     */
    @Bean
    public DataSource dataSourceOne(DataSource masterDataSource) {
        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        dataSourceBean.setXaDataSource((DruidXADataSource) masterDataSource);
        dataSourceBean.setPoolSize(5);
        dataSourceBean.setTestQuery("SELECT 1");
        dataSourceBean.setUniqueResourceName("mydbone");
        return dataSourceBean;
    }

    /**
     * 创建支持 XA 事务的 Atomikos 数据源 mydbone
     * @param salveDataSource
     * @return
     */
    @Bean
    public DataSource dataSourceTwo(DataSource salveDataSource) {
        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        dataSourceBean.setXaDataSource((DruidXADataSource) salveDataSource);
        dataSourceBean.setPoolSize(5);
        dataSourceBean.setTestQuery("SELECT 1");
        dataSourceBean.setUniqueResourceName("mydbtwo");
        return dataSourceBean;
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactoryOne(DataSource dataSourceOne) throws Exception {
        return createSqlSessionFactory(dataSourceOne);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryTwo(@Qualifier("dataSourceTwo") DataSource dataSourceTwo) throws Exception {
        return createSqlSessionFactory(dataSourceTwo);
    }

    /**
     * 自定义会话工程
     * @param dataSource
     * @return
     * @throws Exception
     */
    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(StdOutImpl.class);
        sqlSessionFactoryBean.setConfiguration(configuration);

        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(patternResolver.getResources("classpath:config/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public CustomSqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactoryOne,@Qualifier("sqlSessionFactoryTwo") SqlSessionFactory sqlSessionFactoryTwo) {
        HashMap<Object, SqlSessionFactory> sqlSessionFactoryHashMap = new HashMap<>();
        sqlSessionFactoryHashMap.put(DataSourceType.MASTER.name(),sqlSessionFactoryOne);
        sqlSessionFactoryHashMap.put(DataSourceType.SALVE.name(),sqlSessionFactoryTwo);
        CustomSqlSessionTemplate customSqlSessionTemplate = new CustomSqlSessionTemplate(sqlSessionFactoryOne);
        customSqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryHashMap);
        return customSqlSessionTemplate;
    }
}
