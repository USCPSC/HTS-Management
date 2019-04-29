package gov.cpsc.hts.itds.ui.domain.config;

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
@ComponentScan({"gov.cpsc.hts.itds.ui.domain.dao.impl"})
public class EhCacheConfig {
	 
	@Autowired
     private ResourceLoader resourceLoader;

	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(resourceLoader.getResource("classpath:ehcache.xml"));

		cmfb.setShared(true);
		return cmfb;
	}

}


