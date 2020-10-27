package cz.muni.ics.oidc;

import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Collections;

@Configuration
public class DataBeans {

    @Bean
    @Autowired
    public DataSource dataSource(JdbcProperties jdbcProperties) {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(jdbcProperties.getDriverClassName());
        ds.setJdbcUrl(jdbcProperties.getUrl());
        ds.setUsername(jdbcProperties.getUsername());
        ds.setPassword(jdbcProperties.getPassword());
        return ds;
    }

    @Bean
    public JpaVendorAdapter jpaAdapter() {
        EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
        adapter.setDatabasePlatform("org.eclipse.persistence.platform.database.MySQLPlatform");
        adapter.setShowSql(false);
        return adapter;
    }

    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaAdapter) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setPersistenceProviderClass(PersistenceProvider.class);
        bean.setPackagesToScan("cz.muni.ics.oidc");
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaAdapter);
        bean.setPersistenceUnitName("defaultPersistenceUnit");
        bean.setJpaPropertyMap(Collections.singletonMap("eclipselink.weaving", "false"));
        return bean;

    }

}
