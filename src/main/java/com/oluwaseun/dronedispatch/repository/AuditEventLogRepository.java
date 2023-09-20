package com.oluwaseun.dronedispatch.repository;

import com.oluwaseun.dronedispatch.model.entity.AuditEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuditEventLogRepository extends JpaRepository<AuditEventLog, Long> {
    @Transactional
    @Modifying
    @Query("INSERT INTO AuditEventLog (drone, batteryCapacity, time) SELECT d, d.batteryCapacity, CURRENT_TIMESTAMP FROM Drone d")
    void logBatteryLevels();
}
