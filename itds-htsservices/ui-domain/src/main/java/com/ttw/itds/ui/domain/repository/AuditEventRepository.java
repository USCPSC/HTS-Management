package com.ttw.itds.ui.domain.repository;

import com.ttw.itds.ui.domain.entity.AuditEvent;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findAllByEventDateBetween(DateTime fromDate, DateTime toDate);
}
