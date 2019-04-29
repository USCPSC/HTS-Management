package gov.cpsc.hts.itds.ui.domain.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.cpsc.hts.itds.ui.domain.entity.CpscHtsManagementFileEntity;

public interface CpscHtsManagementFileRepository extends JpaRepository<CpscHtsManagementFileEntity,Long> {

	public Page<CpscHtsManagementFileEntity> findByUsername(String username, Pageable pageable);
    
}
