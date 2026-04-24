package net.isbg.currency.domain.entity.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
public class RequestAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String requestId;

    @Column(nullable = false)
    private long timestamp;

    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommandType command;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public  RequestAudit(String requestId, long timestamp, String clientId,
                        CommandType command, ServiceType serviceType) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.clientId = clientId;
        this.command = command;
        this.serviceType = serviceType;
    }
}