package com.ttw.itds.ui.service.config;

import com.ttw.itds.ui.domain.config.EhCacheConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@Import({com.ttw.itds.ui.domain.config.HibernateJpaConfig.class, EhCacheConfig.class})
@ComponentScan({"com.ttw.itds.ui.service.impl"})
@EnableScheduling
public class ItdsUiServiceConfig {

}
