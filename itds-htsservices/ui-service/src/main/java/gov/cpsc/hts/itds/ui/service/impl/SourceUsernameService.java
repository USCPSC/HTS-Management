package gov.cpsc.hts.itds.ui.service.impl;

import gov.cpsc.hts.itds.ui.domain.entity.SysPropertyEntity;
import gov.cpsc.hts.itds.ui.domain.repository.SysPropertyRepository;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SourceUsernameService {

    private final Logger logger = LoggerFactory.getLogger(SourceUsernameService.class);
    private static final String SYS_PROPERTY_PREFIX = "HTS.SVC.";
    
    @Autowired
    private SysPropertyService sysPropertyService;

    @Autowired
    @Qualifier("sourceUsernameBean2")
    private HashMap<String, String> sourceUsernameMap;

    @Bean(name="sourceUsernameBean2")
    public HashMap<String, String> getSourceUsernameMap() {
    	System.out.println("L22 Creating sourceUsernameMap");
        logger.error("L23 Creating sourceUsernameMap");
        HashMap<String, String> sourceUsernameMap = new HashMap<>();
    	System.out.println("L26");
    	logger.error("L27");
    	sourceUsernameMap.put(HtsGlobalStateEnum.CPSC_CURRENT_WIP.name(), sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_CPSC_CURRENT_WIP)));
    	sourceUsernameMap.put(HtsGlobalStateEnum.ITC_UPLOAD_WIP.name(),   sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_ITC_UPLOAD_WIP)));
    	sourceUsernameMap.put(HtsGlobalStateEnum.FINALIZED_NO_WIP.name(), sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_FINALIZED_NO_WIP)));
    	sourceUsernameMap.put(HtsGlobalStateEnum.IN_TRANSITION.name(),    sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_IN_TRANSITION)));
        return sourceUsernameMap;
    }
    
    private String prefixedPropertyKey(HtsSysPropertyEnum hspe) {
    	return SYS_PROPERTY_PREFIX + hspe.name();
    }

    public String get(String nameOfEnumRepresentingAnyState) {
    	return sourceUsernameMap.get(nameOfEnumRepresentingAnyState);
    }
    
    public String put(String source, String username) {
    	return sourceUsernameMap.put(source, username);
    }

    public Set<String> keySet() {
    	return sourceUsernameMap.keySet();
    }
}
