package com.ttw.itds.ui.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/**
* Java Configuration for EhCache
*/
@Configuration
@EnableCaching
@ComponentScan({"com.ttw.itds.ui.domain.dao.impl"})
public class EhCacheConfig {
	public static final String SystemArgName = "ItdsUi.Config";
	 
	@Autowired
     private ResourceLoader resourceLoader;

	@Autowired
	private Environment environment;
	 
	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(resourceLoader.getResource(environment.getProperty(SystemArgName)+"/ehcache.xml"));

		cmfb.setShared(true);
		return cmfb;
	}

}


