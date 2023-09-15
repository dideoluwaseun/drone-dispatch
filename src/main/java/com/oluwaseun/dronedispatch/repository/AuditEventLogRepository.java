package com.oluwaseun.dronedispatch.repository;

import com.oluwaseun.dronedispatch.model.entity.AuditEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEventLogRepository extends JpaRepository<AuditEventLog, Long> {
}
