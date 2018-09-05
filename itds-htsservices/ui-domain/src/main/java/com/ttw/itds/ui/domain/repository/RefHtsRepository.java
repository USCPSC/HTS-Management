package com.ttw.itds.ui.domain.repository;

import com.ttw.itds.ui.domain.entity.RefHtsEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefHtsRepository extends JpaRepository<RefHtsEntity,String> {
	public List<RefHtsEntity> findByHts(String hts);
}
