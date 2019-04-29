package gov.cpsc.hts.itds.ui.domain.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@EnableJpaRepositories("gov.cpsc.hts.itds.ui.domain.repository")
@ComponentScan({"gov.cpsc.hts.itds.ui.domain.dao.impl","gov.cpsc.hts.itds.ui.domain.entity"})
public class HibernateJpaConfig {

	private static final Logger logger = LoggerFactory.getLogger(HibernateJpaConfig.class);

	@Autowired
	private Environment environment;
	
    @Value("${hts.db.defaultSchema}")
    private String defaultSchema;

	@Bean
	public ComboPooledDataSource dataSource() throws Exception {
	    return new ComboPooledDataSource(
    		environment.getActiveProfiles().length > 0?environment.getActiveProfiles()[0]:"dev");
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(ComboPooledDataSource datasource) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(datasource);
		emf.setPackagesToScan(new String[] { "gov.cpsc.hts.itds.ui.domain.entity" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(hibernateProperties());

		return emf;
	}

	@Bean
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
		return new JpaTransactionManager(emf.getObject());
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
		properties.setProperty("hibernate.default_schema", environment.getProperty("hibernate.default_schema", defaultSchema));
		return properties;
	}
	
}
