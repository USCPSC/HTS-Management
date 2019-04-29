package gov.cpsc.hts.itds.ui.service.impl;

import gov.cpsc.hts.itds.ui.domain.entity.SysPropertyEntity;
import gov.cpsc.hts.itds.ui.domain.repository.SysPropertyRepository;
import java.util.*;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class SysPropertyService {

    private final Logger logger = LoggerFactory.getLogger(SysPropertyService.class);
    private HashMap<String, SysPropertyEntity> sysPropertyCache = new HashMap<>();

    @Autowired
    private SysPropertyRepository sysPropertyRepository;

    public ServiceResponse<List<SysPropertyEntity>> getSysPropertyList() {
        ServiceResponse<List<SysPropertyEntity>> response = new ServiceResponse<>();
        try {
            List<SysPropertyEntity> listSysProperty = sysPropertyRepository.findAll();
            response.setValue(listSysProperty);
        } catch (Exception e) {
            logger.error("error getting Sys Property list", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ServiceResponse<SysPropertyEntity> getSysPropertyByName(String propname) {
        ServiceResponse<SysPropertyEntity> response = new ServiceResponse<>();
        SysPropertyEntity sysPropertyFromCache = sysPropertyCache.get(propname);
        if (sysPropertyFromCache == null) {
        	try {
        		SysPropertyEntity speForExample = new SysPropertyEntity();
        		speForExample.setName(propname);
        		Optional<SysPropertyEntity> optSysPropertyFromTable = sysPropertyRepository.findOne(Example.of(speForExample));
        		if (optSysPropertyFromTable.isPresent()) {
        			sysPropertyCache.put(propname, optSysPropertyFromTable.get());
        			response.setValue(optSysPropertyFromTable.get());
        		} else { // neither cached nor in table, so return key alongside the empty String as the value:
        			speForExample.setValue("");
        			response.setValue(speForExample);
        		}
        	} catch (Exception e) {
	            logger.error("error getting single sysProperty: " + propname, e);
	            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        	}
        } else {
        	response.setValue(sysPropertyFromCache);
        }
        return response;
    }

    public String getSysPropertyStringByName(String propname) {
        ServiceResponse<SysPropertyEntity> response = getSysPropertyByName(propname);
		String rtn = null;
		if (response.isOK()) {
			if (response.getValue() != null) {
				rtn = response.getValue().getValue();
			}
		}
		return rtn;
    }

    public Boolean getSysPropertyBooleanByName(String propname) {
        ServiceResponse<SysPropertyEntity> response = getSysPropertyByName(propname);
		Boolean rtn = null;
		if (response.isOK()) {
			if (response.getValue() != null) {
				String val = response.getValue().getValue();
				if (StringUtils.isNotBlank(val)) {
					try {
						rtn = Boolean.valueOf(val);
					} catch (Exception e) {
					}
				}
			}
		}
		return rtn;
    }

    public Integer getSysPropertyIntegerByName(String propname) {
        ServiceResponse<SysPropertyEntity> response = getSysPropertyByName(propname);
		Integer rtn = null;
		if (response.isOK()) {
			if (response.getValue() != null) {
				String val = response.getValue().getValue();
				if (StringUtils.isNotBlank(val)) {
					try {
						rtn = Integer.valueOf(val);
					} catch (Exception e) {
					}
				}
			}
		}
		return rtn;
    }

    @Transactional
    public ServiceResponse<SysPropertyEntity> saveSysProperty(SysPropertyEntity sysPropertyNew) {
        ServiceResponse<SysPropertyEntity> response = new ServiceResponse<>();
        try {
            SysPropertyEntity sysPropertySave;
            SysPropertyEntity withNameAloneAsExample = new SysPropertyEntity();
            withNameAloneAsExample.setName(sysPropertyNew.getName());
            Optional<SysPropertyEntity> oldSysPropertyOrEmpty = sysPropertyRepository.findOne(Example.of(withNameAloneAsExample));
            if (oldSysPropertyOrEmpty.isPresent()) {
                // existing property
                sysPropertySave = oldSysPropertyOrEmpty.get();
                sysPropertySave.setValue(sysPropertyNew.getValue());
                sysPropertySave.setLastUpdateTimestamp(sysPropertyNew.getLastUpdateTimestamp());
                sysPropertySave.setLastUpdateUserId(sysPropertyNew.getLastUpdateUserId());
            } else {
                sysPropertySave = sysPropertyNew;
            }
            SysPropertyEntity sysPropertyRtn = sysPropertyRepository.saveAndFlush(sysPropertySave);
            response.setValue(sysPropertyRtn);
        } catch (Exception e) {
            logger.error("error saving sysProperty", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional
    public ServiceResponse deleteSysProperty(String propname) {
        ServiceResponse response = new ServiceResponse<>();
        try {
            sysPropertyRepository.deleteById(propname);
            response.setResponseCode(ServiceResponseCodeEnum.OK);
        } catch (Exception e) {
            logger.error("error saving sysProperty", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}
