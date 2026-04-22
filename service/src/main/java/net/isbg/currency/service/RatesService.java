package net.isbg.currency.service;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.domain.repository.RatesRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RatesService {

    private final RatesRepository ratesRepository;

    public RatesService(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    public Rates getLatestRates() {
        return ratesRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new NoSuchElementException("No rates records found"));
    }
}
