package gov.cpsc.hts.itds.ui.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.cpsc.hts.itds.ui.domain.entity.SysPropertyEntity;

public interface SysPropertyRepository extends JpaRepository<SysPropertyEntity,String>{

}
