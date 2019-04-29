package gov.cpsc.hts.itds.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import gov.cpsc.hts.itds.ui.shared.dto.ServerInfoDto;


@SpringBootApplication
@Configuration
@Import({gov.cpsc.hts.itds.ui.service.config.ItdsUiServiceConfig.class})

public class AppConfig {

	@Autowired Environment env;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer() ;
	}
	
	private ServerInfoDto serverInfoDto;

	@Bean(name="serverInfo")
	public ServerInfoDto getServerInfoDto() {
		if( serverInfoDto == null){
			serverInfoDto = new ServerInfoDto();
			serverInfoDto.setServerName(env.getProperty("server_name"));
			serverInfoDto.setServerVersion(env.getProperty("server_version"));
			serverInfoDto.setMinutesBeforeSessionTimeoutWarning(env.getProperty("minutes_before_session_timeout_warning"));
			serverInfoDto.setMinutesAfterSessionTimeoutWarning(env.getProperty("minutes_after_session_timeout_warning"));
			serverInfoDto.setIfsViewSampleUrl(env.getProperty("ifs.view_sample_url"));
			serverInfoDto.setIfsCreateSampleUrl(env.getProperty("ifs.create_sample_url"));
			serverInfoDto.setRuleEngineUiUrl(env.getProperty("re.rule_engine_ui_url"));
			serverInfoDto.setRamApiUrlPrefix(env.getProperty("ramApi.url.prefix"));
			serverInfoDto.setRamApiUrlListNationalOpsPartial(env.getProperty("ramApi.url.listNationalOps.partial"));
		}
		return serverInfoDto;
	}
	

}