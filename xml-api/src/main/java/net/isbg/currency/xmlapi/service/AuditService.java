package net.isbg.currency.xmlapi.service;

import net.isbg.currency.domain.entity.audit.CommandType;
import net.isbg.currency.domain.entity.audit.RequestAudit;
import net.isbg.currency.domain.entity.audit.ServiceType;
import net.isbg.currency.domain.repository.RequestAuditRepository;
import net.isbg.currency.xmlapi.dto.CommandRequest;
import net.isbg.currency.xmlapi.exception.DuplicateRequestException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuditService {

    private final RequestAuditRepository auditRepository;

    public AuditService(RequestAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void auditCurrent(CommandRequest request) {
        checkDuplicate(request.getRequestId());
        auditRepository.save(new RequestAudit(
                request.getRequestId(),
                Instant.now().toEpochMilli(),
                request.getGet().getConsumer(),
                CommandType.CURRENT,
                ServiceType.EXT_SERVICE_2
        ));
    }

    public void auditHistory(CommandRequest request) {
        checkDuplicate(request.getRequestId());
        auditRepository.save(new RequestAudit(
                request.getRequestId(),
                Instant.now().toEpochMilli(),
                request.getHistory().getConsumer(),
                CommandType.HISTORY,
                ServiceType.EXT_SERVICE_2
        ));
    }

    private void checkDuplicate(String requestId) {
        if (auditRepository.existsByRequestId(requestId)) {
            throw new DuplicateRequestException(requestId);
        }
    }
}