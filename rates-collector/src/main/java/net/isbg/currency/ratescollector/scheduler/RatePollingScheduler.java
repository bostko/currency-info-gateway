package net.isbg.currency.ratescollector.scheduler;

import net.isbg.currency.ratescollector.dto.LatestRatesResponse;
import net.isbg.currency.ratescollector.gateway.FixerIoGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatePollingScheduler {

    private static final Logger log = LoggerFactory.getLogger(RatePollingScheduler.class);

    private final FixerIoGateway fixerIoGateway;

    public RatePollingScheduler(FixerIoGateway fixerIoGateway) {
        this.fixerIoGateway = fixerIoGateway;
    }

    @Scheduled(fixedRateString = "${net.isbg.currency.collector.refresh_interval}")
    public void pollLatestRates() throws Exception {
        LatestRatesResponse response = fixerIoGateway.getLatest();
        if (response.success()) {
            log.info("Polled rates for base '{}' on {}: {}", response.base(), response.date(), response.rates());
        } else {
            log.warn("Poll failed: {}", response.error() != null ? response.error().info() : "unknown error");
        }
    }
}