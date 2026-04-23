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
    private Instant timestamp;

    @Column(nullable = false)
    private long clientId;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommandType command;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    /** Non-null only for {@link CommandType#HISTORY} requests. */
    private Integer period;

    public RequestAudit(String requestId, Instant timestamp, long clientId, String currency,
                        CommandType command, ServiceType serviceType, Integer period) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.clientId = clientId;
        this.currency = currency;
        this.command = command;
        this.serviceType = serviceType;
        this.period = period;
    }
}