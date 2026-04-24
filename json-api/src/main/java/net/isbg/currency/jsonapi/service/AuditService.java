package net.isbg.currency.jsonapi.service;

import net.isbg.currency.domain.entity.audit.CommandType;
import net.isbg.currency.domain.entity.audit.RequestAudit;
import net.isbg.currency.domain.entity.audit.ServiceType;
import net.isbg.currency.domain.repository.RequestAuditRepository;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import net.isbg.currency.jsonapi.exception.DuplicateRequestException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuditService {

    private final RequestAuditRepository auditRepository;

    public AuditService(RequestAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void audit(CurrentRequest request) {
        checkDuplicate(request.requestId());
        auditRepository.save(new RequestAudit(
                request.requestId(),
                request.timestamp(),
                Long.parseLong(request.client()),
                request.currency(),
                CommandType.CURRENT,
                ServiceType.EXT_SERVICE_1,
                null
        ));
    }

    public void audit(HistoryRequest request) {
        checkDuplicate(request.requestId());
        auditRepository.save(new RequestAudit(
                request.requestId(),
                request.timestamp(),
                Long.parseLong(request.client()),
                request.currency(),
                CommandType.HISTORY,
                ServiceType.EXT_SERVICE_1,
                request.period()
        ));
    }

    private void checkDuplicate(String requestId) {
        if (auditRepository.existsByRequestId(requestId)) {
            throw new DuplicateRequestException(requestId);
        }
    }
}