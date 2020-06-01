package com.groupchatback.config;

import com.groupchatback.util.EnvConfigUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/** Source: https://stackoverflow.com/questions/44127381/how-to-set-jpa-entitymanagerfactory-for-persistence-unit-not-to-default **/

@Configuration
public class JPAConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan();
        em.setPersistenceUnitName("hibernate-persistence-unit-default");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(EnvConfigUtil.getProperty("JDBC_URL"));
        dataSource.setUsername(EnvConfigUtil.getProperty("JDBC_USER"));

        return dataSource;
    }
}
