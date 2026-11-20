package com.physio.springmvc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Unit 3.2.5: Spring JPA 3.1 & Hibernate 6 Architecture Configuration.
 * Configures HikariCP DataSource, LocalContainerEntityManagerFactoryBean,
 * and JpaTransactionManager for Declarative Transaction Management.
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class JpaConfig {

    private final Environment env;

    @Autowired
    public JpaConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("PhysioSpringHikariPool");
        config.setDriverClassName(env.getProperty("jdbc.driverClassName", "org.postgresql.Driver"));
        config.setJdbcUrl(env.getProperty("jdbc.url", "jdbc:postgresql://localhost:5432/physio_db"));
        config.setUsername(env.getProperty("jdbc.username", "postgres"));
        config.setPassword(env.getProperty("jdbc.password", "roshan123"));
        config.setMaximumPoolSize(Integer.parseInt(env.getProperty("hikari.maximumPoolSize", "10")));
        config.setMinimumIdle(Integer.parseInt(env.getProperty("hikari.minimumIdle", "2")));
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.physio.springmvc.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("hibernate.show_sql", "true")));
        vendorAdapter.setGenerateDdl(false);
        em.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"));
        jpaProperties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql", "true"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "validate"));
        em.setJpaProperties(jpaProperties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public static PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
