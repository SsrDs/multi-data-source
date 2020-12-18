package com.wzs.multidatasource.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * @Description:
 * @Auther: wuzs
 * @Date: 2020/12/18 16:51
 * @Version: 1.0
 */
@Configuration
@EnableTransactionManagement
public class XATransactionManagerConfig {

    @Bean
    public UserTransaction userTransaction() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

    @Bean
    public TransactionManager atomikosTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager(UserTransaction userTransaction,TransactionManager transactionManager) {
        return new JtaTransactionManager(userTransaction,transactionManager);
    }
}
