package gov.cpsc.hts.itds.ui.service.impl;

import gov.cpsc.hts.itds.ui.domain.entity.RefRoleEntity;
import gov.cpsc.hts.itds.ui.domain.repository.RefRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefRoleService {
    private static final Logger logger = LoggerFactory.getLogger(RefRoleService.class);

    @Autowired
    private RefRoleRepository refRoleRepository;

    public ServiceResponse<List<RefRoleEntity>> getRefRoles() {
        ServiceResponse<List<RefRoleEntity>> response = new ServiceResponse<>();
        try {
            List<RefRoleEntity> roles = refRoleRepository.findAll();
            response.setValue(roles);
        } catch (Exception e) {
            logger.error("error getting role list",e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
