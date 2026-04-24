package net.isbg.currency.jsonapi.service;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.domain.repository.RatesRepository;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CurrencyService {

    private final RatesRepository ratesRepository;

    public CurrencyService(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    public Rates getCurrent(CurrentRequest request) {
        Rates latest = ratesRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new NoSuchElementException("No rates available"));
        Double rate = latest.getRates().get(request.currency());
        if (rate == null) {
            throw new NoSuchElementException("Currency not found: " + request.currency());
        }
        return latest;
    }

    public List<Rates> getHistory(HistoryRequest request) {
        long fromSecs = Instant.now().minus(request.period(), ChronoUnit.HOURS).toEpochMilli();
        return ratesRepository.findByTimestampGreaterThanEqualOrderByTimestampAsc(fromSecs)
                .stream()
                .filter(r -> r.getRates().containsKey(request.currency()))
                .toList();
    }
}