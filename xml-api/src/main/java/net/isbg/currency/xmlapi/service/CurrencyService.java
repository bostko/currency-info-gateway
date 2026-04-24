package net.isbg.currency.xmlapi.service;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.domain.repository.RatesRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CurrencyService {

    private final RatesRepository ratesRepository;

    public CurrencyService(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    public Rates getCurrent(String currency) {
        Rates latest = ratesRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new NoSuchElementException("No rates available"));
        if (!latest.getRates().containsKey(currency)) {
            throw new NoSuchElementException("Currency not found: " + currency);
        }
        return latest;
    }

    public List<Rates> getHistory(String currency, int periodHours) {
        long fromSecs = Instant.now().minusSeconds((long) periodHours * 3600).getEpochSecond();
        return ratesRepository.findByTimestampGreaterThanEqualOrderByTimestampAsc(fromSecs)
                .stream()
                .filter(r -> r.getRates().containsKey(currency))
                .toList();
    }
}