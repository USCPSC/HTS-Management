package gov.cpsc.hts.itds.ui.domain.repository;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import gov.cpsc.hts.itds.ui.domain.entity.AuditEvent;

import java.util.List;

public interface AuditItdsEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findAllByEventDateBetween(DateTime fromDate, DateTime toDate);
}
