package net.isbg.currency.ratescollector.service;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.domain.repository.RatesRepository;
import net.isbg.currency.ratescollector.gateway.FixerIoGateway;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RatesService {
    private final RatesRepository ratesRepository;

    private final FixerIoGateway fixerIoGateway;

    public RatesService(RatesRepository ratesRepository, FixerIoGateway fixerIoGateway) {
        this.ratesRepository = ratesRepository;
        this.fixerIoGateway = fixerIoGateway;
    }

    public Rates getLatestRates() {
        return ratesRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new NoSuchElementException("No rates records found"));
    }

    public void add(Rates rates) {
        ratesRepository.save(rates);
    }
}
