package com.ttw.itds.ui.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ttw.itds.ui.domain.entity.AuditRecordEntity;

public interface AuditRecordRepository extends JpaRepository<AuditRecordEntity,Long>{


}
