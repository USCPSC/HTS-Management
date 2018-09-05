package com.ttw.itds.ui.service.impl;

import com.ttw.itds.ui.domain.entity.RefRoleEntity;
import com.ttw.itds.ui.domain.repository.RefRoleRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class RefRoleService {
    private final Logger log = Logger.getLogger(RefRoleService.class);

    @Inject
    private RefRoleRepository refRoleRepository;

    public ServiceResponse<List<RefRoleEntity>> getRefRoles() {
        ServiceResponse<List<RefRoleEntity>> response = new ServiceResponse<>();
        try {
            List<RefRoleEntity> roles = refRoleRepository.findAll();
            response.setValue(roles);
        } catch (Exception e) {
            log.error("error getting role list",e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
