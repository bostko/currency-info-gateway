package net.isbg.currency.jsonapi.service;

import net.isbg.currency.jsonapi.dto.CurrencyRateResponse;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CurrencyService {
    public CurrencyRateResponse getCurrent(CurrentRequest request) {
        return new CurrencyRateResponse(request.currency(), new BigDecimal("1.9558"), Instant.now().minus(30, ChronoUnit.DAYS));
    }
    public List<CurrencyRateResponse> getHistory(HistoryRequest request) {
        return List.of();
    }
}