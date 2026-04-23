package net.isbg.currency.domain.repository;

import net.isbg.currency.domain.entity.audit.RequestAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestAuditRepository extends JpaRepository<RequestAudit, Long> {

    boolean existsByRequestId(String requestId);
}