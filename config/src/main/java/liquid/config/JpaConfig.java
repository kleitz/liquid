package liquid.config;

import net.sf.ehcache.management.ManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 9:06 PM
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({
        "liquid.persistence.repository",
        "liquid.operation.repository",
        "liquid.container.persistence.repository",
        "liquid.accounting.persistence.repository",
        "liquid.purchase.persistence.repository",
        "liquid.order.persistence.repository",
        "liquid.transport.persistence.repository",
        "liquid.purchase.persistence.repository"})
@Import(PropertyPlaceholderConfig.class)
public class JpaConfig {
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("javax.persistence.validation.mode", "none");
        jpaProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
        jpaProperties.setProperty("hibernate.cache.use_query_cache", "true");
        jpaProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(
                "liquid.persistence.domain",
                "liquid.operation.domain",
                "liquid.container.domain",
                "liquid.order.domain",
                "liquid.order.persistence.domain",
                "liquid.transport.persistence.domain", // FIXME
                "liquid.transport.domain",
                "liquid.charge.persistence.domain",
                "liquid.accounting.persistence.domain",
                "liquid.purchase.persistence.domain");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    // The following three beans are used for monitoring of ehcache.
    @Bean
    public MBeanServerFactoryBean mBeanServerFactory() {
        MBeanServerFactoryBean mBeanServerFactory = new MBeanServerFactoryBean();
        mBeanServerFactory.setLocateExistingServerIfPossible(true);
        return mBeanServerFactory;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactory() {
        EhCacheManagerFactoryBean ehCacheManagerFactory = new EhCacheManagerFactoryBean();
        ehCacheManagerFactory.setShared(true);
        return ehCacheManagerFactory;
    }

    @Bean(initMethod = "init", destroyMethod = "dispose")
    public ManagementService managementService() {
        return new ManagementService(ehCacheManagerFactory().getObject(), mBeanServerFactory().getObject(), true, true, true, true);
    }
}
