package net.isbg.currency.countrytour.service;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.domain.repository.RatesRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ExchangeRateService {

    private final RatesRepository ratesRepository;

    public ExchangeRateService(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    public double convert(double amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        Rates latest = ratesRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new NoSuchElementException("No exchange rates available"));
        Map<String, Double> cross = latest.crossRatesFor(fromCurrency);
        Double rate = cross.get(toCurrency);
        if (rate == null) {
            return amount;
        }
        return amount * rate;
    }
}