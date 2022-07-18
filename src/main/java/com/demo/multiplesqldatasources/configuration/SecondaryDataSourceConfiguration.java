package com.demo.multiplesqldatasources.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.demo.multiplesqldatasources.repository.secondary"},
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDataSourceConfiguration extends DataSourceConfiguration {

    public SecondaryDataSourceConfiguration(JpaProperties jpaProperties,
                                            HibernateProperties hibernateProperties,
                                            ConfigurableListableBeanFactory beanFactory,
                                            ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        super(jpaProperties, hibernateProperties, beanFactory, hibernatePropertiesCustomizers);
    }

    @Bean("secondaryJpaProperties")
    @Override
    @ConfigurationProperties("spring.jpa.secondary")
    public JpaProperties getJpaProperties() {
        return new JpaProperties();
    }

    @Bean("secondaryDatasourceProperties")
    @Override
    @ConfigurationProperties("spring.datasource.secondary")
    public DataSourceProperties getDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Override
    public DataSource getDataSource() {
        return getHikariDataSource();
    }

    @Bean("secondaryEntityManagerFactory")
    @Override
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        return super.getEntityManagerFactory();
    }

    @Bean("secondaryTransactionManager")
    @Override
    public PlatformTransactionManager getTransactionManager() {
        return super.getTransactionManager();
    }
}
