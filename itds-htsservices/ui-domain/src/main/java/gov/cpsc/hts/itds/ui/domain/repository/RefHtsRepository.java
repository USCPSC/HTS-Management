package gov.cpsc.hts.itds.ui.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.cpsc.hts.itds.ui.domain.entity.RefHtsEntity;

public interface RefHtsRepository extends JpaRepository<RefHtsEntity,String> {
	public List<RefHtsEntity> findByHts(String hts);
}
