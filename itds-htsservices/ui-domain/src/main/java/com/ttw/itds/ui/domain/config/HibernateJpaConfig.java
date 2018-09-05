package com.ttw.itds.ui.domain.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
* Java Configuration for JPA, Hibernate implementation
*/
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.ttw.itds.ui.domain.repository")
@ComponentScan({"com.ttw.itds.ui.domain.dao.impl","com.ttw.itds.ui.domain.entity"})
@PropertySource({ "${" + HibernateJpaConfig.SystemArgName + "}/itds-datasource.properties" })
public class HibernateJpaConfig {
	private static final Logger logger = Logger.getLogger(HibernateJpaConfig.class);
	public static final String SystemArgName = "ItdsUi.Config";

	@Autowired
	private Environment environment;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setPackagesToScan(new String[] { "com.ttw.itds.ui.domain.entity" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(hibernateProperties());

		return emf;
	}

	@Bean
	public DataSource dataSource() {
		
			ComboPooledDataSource ds = new ComboPooledDataSource();
					
					try {
						//basic
						ds.setDriverClass(environment.getRequiredProperty("jdbc.driverClassName"));
						ds.setJdbcUrl( environment.getRequiredProperty("jdbc.url") );
						// 	user and password
						ds.setUser( environment.getRequiredProperty("jdbc.username") );
						ds.setPassword( environment.getRequiredProperty("jdbc.password") );
						
						//pool 
						ds.setInitialPoolSize( getConnPoolProp("pool.initialPoolSize",3) );
						ds.setMinPoolSize( getConnPoolProp("pool.minPoolSize",6) );
						ds.setMaxPoolSize( getConnPoolProp("pool.maxPoolSize",10) );
						ds.setAcquireIncrement( getConnPoolProp("pool.acquireIncrement",3) );
						ds.setMaxStatements( getConnPoolProp("pool.maxStatements",0) );
						
						//retries
						ds.setAcquireRetryAttempts( getConnPoolProp("retry.acquireRetryAttempts",10) );
						ds.setAcquireRetryDelay( getConnPoolProp("retry.acquireRetryDelay",1000));
						ds.setBreakAfterAcquireFailure( getConnPoolProp("retry.breakAfterAcquireFailure",false) );
						
						//refreshing conns
						ds.setMaxIdleTime( getConnPoolProp("refresh.maxIdleTime",180) );
						ds.setMaxConnectionAge( getConnPoolProp("refresh.maxConnectionAge",10) );
						
						//timeouts & testing
						ds.setCheckoutTimeout( getConnPoolProp("ping.checkoutTimeout",5000) );
						ds.setIdleConnectionTestPeriod( getConnPoolProp("ping.idleConnectionTestPeriod",60));
						ds.setTestConnectionOnCheckin( getConnPoolProp("ping.testConnectionOnCheckin",true));
						ds.setTestConnectionOnCheckout( getConnPoolProp("ping.testConnectionOnCheckout",true));
						ds.setPreferredTestQuery( getConnPoolProp("ping.preferredTestQuery") );
						
						ds.setNumHelperThreads( getConnPoolProp("numHelperThreads",10) );
						ds.setMaxAdministrativeTaskTime( getConnPoolProp("maxAdministrativeTaskTime",0) );
						ds.setStatementCacheNumDeferredCloseThreads( getConnPoolProp("statementCacheNumDeferredCloseThreads",0) );
						
						return ds;
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}		
					return ds;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect","org.hibernate.dialect.SQLServer2012Dialect"));
		properties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show.sql", "false"));
		properties.setProperty("hibernate.format_sql", environment.getProperty("hibernate.format.sql", "false"));
		properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto", "validate"));
		properties.setProperty("hibernate.default_schema", environment.getProperty("hibernate.default_schema", "EXAMPLE"));
		return properties;
	}
	
	protected int getConnPoolProp(String keySuffix,int fallback){
		String val = environment.getProperty("ramui.db."+keySuffix);
		if( StringUtils.isEmpty(val) ){
			logger.warn("Got blank value of key ["+"ramui.db."+keySuffix+"], apply fallback value ("+fallback+").....");
			return fallback;
		}
		
		return Integer.parseInt(val.trim());
	}
	
	protected boolean getConnPoolProp(String keySuffix,boolean fallback){
		String val = environment.getProperty("ramui.db."+keySuffix);
		if( StringUtils.isEmpty(val) ){
			logger.warn("Got blank value of key ["+"ramui.db."+keySuffix+"], apply fallback value ("+fallback+").....");
			return fallback;
		}
		
		return Boolean.parseBoolean(val.trim());
	}
	
	protected String getConnPoolProp(String keySuffix){
		return environment.getProperty("ramui.db."+keySuffix);
	}
}