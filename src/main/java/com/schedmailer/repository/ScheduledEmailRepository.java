package com.schedmailer.repository;

import com.schedmailer.domain.entity.ScheduledEmail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledEmailRepository extends JpaRepository<ScheduledEmail, UUID> {
    @Query("""
            SELECT e FROM ScheduledEmail e
            WHERE (e.status = 'PENDING' OR e.status = 'RETRYING')
              AND (e.shouldSendAt IS NULL OR e.shouldSendAt <= :now)
            """)
    List<ScheduledEmail> findPendingOrRetryingDue(@Param("now") ZonedDateTime now);
}
