package com.demo.multiplesqldatasources.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

public abstract class DataSourceConfiguration {
    private final JpaProperties commonJpaProperties;
    private final HibernateProperties hibernateProperties;
    private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    public DataSourceConfiguration(
            JpaProperties jpaProperties,
            HibernateProperties hibernateProperties,
            ConfigurableListableBeanFactory beanFactory,
            ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers
    ) {
        this.commonJpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
        this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(beanFactory, hibernatePropertiesCustomizers.orderedStream().collect(Collectors.toList()));
    }

    public abstract JpaProperties getJpaProperties();

    public abstract DataSourceProperties getDataSourceProperties();

    public abstract DataSource getDataSource();

    protected LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(vendorAdapter, getCombinedVendorProperties(getJpaProperties()), null);
        return builder
                .dataSource(getDataSource())
                .packages("com.demo.multiplesqldatasources.model.entity")
                .build();
    }

    protected DataSource getHikariDataSource() {
        return getDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    protected PlatformTransactionManager getTransactionManager() {
        return new JpaTransactionManager(Objects.requireNonNull(getEntityManagerFactory().getObject()));
    }

    private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(ConfigurableListableBeanFactory beanFactory, List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        if (ClassUtils.isPresent("org.hibernate.resource.beans.container.spi.BeanContainer",
                getClass().getClassLoader())) {
            customizers.add((properties) -> properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory)));
        }
        customizers.addAll(hibernatePropertiesCustomizers);
        return customizers;
    }

    private Map<String, Object> getCommonVendorProperties() {
        return new LinkedHashMap<>(this.hibernateProperties.determineHibernateProperties(this.commonJpaProperties.getProperties(), new HibernateSettings().hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)));
    }

    private Map<String, Object> getCombinedVendorProperties(JpaProperties jpaProperties) {
        Map<String, Object> combinedVendorProperties = new HashMap<>(getCommonVendorProperties());
        jpaProperties.getProperties().forEach(combinedVendorProperties::put);
        return combinedVendorProperties;
    }
}
