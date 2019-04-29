package gov.cpsc.hts.itds.ui.service.config;

import gov.cpsc.hts.itds.ui.domain.config.EhCacheConfig;

import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@Import({gov.cpsc.hts.itds.ui.domain.config.HibernateJpaConfig.class, EhCacheConfig.class})
@ComponentScan(basePackages = {"gov.cpsc.hts.itds.ui.service.impl", "gov.cpsc.hts.itds.ui.camel", "gov.cpsc.itds.common"})
@EnableScheduling
public class ItdsUiServiceConfig extends CamelConfiguration {
	
    @Configuration
    @Profile("default")
    @PropertySource("classpath:application.properties")
    static class Defaults
    { }
    @Configuration
    @Profile("dev")
    @PropertySource("classpath:application-dev.properties")
    static class Dev
    { }
    @Configuration
    @Profile("qa")
    @PropertySource("classpath:application-qa.properties")
    static class Qa
    { }
    @Configuration
    @Profile("prod")
    @PropertySource("classpath:application-prod.properties")
    static class Prod
    { }
    @Configuration
    @Profile("nonpublic")
    @PropertySource("classpath:application-nonpublic.properties")
    static class Nonpublic
    { }

}
