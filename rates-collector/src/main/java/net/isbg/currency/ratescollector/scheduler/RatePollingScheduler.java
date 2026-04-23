package net.isbg.currency.ratescollector.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.isbg.currency.ratescollector.converter.LatestRatesResponseConverter;
import net.isbg.currency.ratescollector.dto.LatestRatesResponse;
import net.isbg.currency.ratescollector.gateway.FixerIoGateway;
import net.isbg.currency.ratescollector.service.RatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatePollingScheduler {

    private static final Logger log = LoggerFactory.getLogger(RatePollingScheduler.class);

    private final FixerIoGateway fixerIoGateway;
    private final RatesService ratesService;
    private final LatestRatesResponseConverter converter;

    public RatePollingScheduler(FixerIoGateway fixerIoGateway, RatesService ratesService, LatestRatesResponseConverter converter) {
        this.fixerIoGateway = fixerIoGateway;
        this.ratesService = ratesService;
        this.converter = converter;
    }

    @Scheduled(fixedRateString = "${net.isbg.currency.collector.refresh_interval}")
    public void pollLatestRates() throws JsonProcessingException {
        LatestRatesResponse response = fixerIoGateway.getLatest();
        if (response.success()) {
            log.info("Polled rates for base '{}' on {}: {}", response.base(), response.date(), response.rates());
            ratesService.add(converter.toRates(response));
        } else {
            log.warn("Poll failed: {}", response.error() != null ? response.error().info() : "unknown error");
        }
    }
}
