package net.isbg.currency.jsonapi.service;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.domain.repository.RatesRepository;
import net.isbg.currency.jsonapi.dto.CurrencyRateResponse;
import net.isbg.currency.jsonapi.dto.CurrentRateResponse;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
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

    // TODO return entity and move dto mapping
    public CurrentRateResponse getCurrent(CurrentRequest request) {
        Rates latest = ratesRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new NoSuchElementException("No rates available"));
        Double rate = latest.getRates().get(request.currency());
        if (rate == null) {
            throw new NoSuchElementException("Currency not found: " + request.currency());
        }
        return new CurrentRateResponse(
                request.currency(),
                rate,
                latest.getBase(),
                latest.getDate(),
                Instant.ofEpochSecond(latest.getTimestamp())
        );
    }

    public List<CurrencyRateResponse> getHistory(HistoryRequest request) {
        return List.of();
    }
}