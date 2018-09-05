package com.ttw.itds.ui.service.impl;

import com.ttw.itds.ui.domain.entity.SysPropertyEntity;
import com.ttw.itds.ui.domain.repository.SysPropertyRepository;
import java.util.*;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SysPropertyService {

    private final Logger log = Logger.getLogger(SysPropertyService.class);

    @Inject
    private SysPropertyRepository sysPropertyRepository;

    public ServiceResponse<List<SysPropertyEntity>> getSysPropertyList() {
        ServiceResponse<List<SysPropertyEntity>> response = new ServiceResponse<>();
        try {
            List<SysPropertyEntity> listSysProperty = sysPropertyRepository.findAll();
            response.setValue(listSysProperty);
        } catch (Exception e) {
            log.error("error getting Sys Property list", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;

    }

    public ServiceResponse<SysPropertyEntity> getSysPropertyByName(String propname) {
        ServiceResponse<SysPropertyEntity> response = new ServiceResponse<>();
        try {
            SysPropertyEntity sysProperty = sysPropertyRepository.findOne(propname);
            response.setValue(sysProperty);
        } catch (Exception e) {
            log.error("error getting single sysProperty: " + propname, e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
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
            SysPropertyEntity sysPropertyOld = sysPropertyRepository.findOne(sysPropertyNew.getName());
            if (sysPropertyOld != null) {
                // existing property
                sysPropertySave = sysPropertyOld;
                sysPropertySave.setValue(sysPropertyNew.getValue());
                sysPropertySave.setLastUpdateTimestamp(sysPropertyNew.getLastUpdateTimestamp());
                sysPropertySave.setLastUpdateUserId(sysPropertyNew.getLastUpdateUserId());
                
            } else {
                sysPropertySave = sysPropertyNew;
            }
            SysPropertyEntity sysPropertyRtn = sysPropertyRepository.saveAndFlush(sysPropertySave);
            response.setValue(sysPropertyRtn);
        } catch (Exception e) {
            log.error("error saving sysProperty", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional
    public ServiceResponse deleteSysProperty(String propname) {
        ServiceResponse response = new ServiceResponse<>();
        try {
            sysPropertyRepository.delete(propname);
            response.setResponseCode(ServiceResponseCodeEnum.OK);
        } catch (Exception e) {
            log.error("error saving sysProperty", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}
