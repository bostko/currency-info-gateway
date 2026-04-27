package net.isbg.currency.jsonapi.controller;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import net.isbg.currency.jsonapi.dto.RateResponse;
import net.isbg.currency.jsonapi.exception.DuplicateRequestException;
import net.isbg.currency.jsonapi.service.AuditService;
import net.isbg.currency.jsonapi.service.CurrencyService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json_api")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final AuditService auditService;

    public CurrencyController(CurrencyService currencyService, AuditService auditService) {
        this.currencyService = currencyService;
        this.auditService = auditService;
    }

    @PostMapping("/current")
    public RateResponse current(@RequestBody CurrentRequest request) {
        auditService.audit(request);
        return toRateResponse(currencyService.getCurrent(request), request.currency());
    }

    @PostMapping("/history")
    public List<RateResponse> history(@RequestBody HistoryRequest request, Pageable pageable) {
        auditService.audit(request);
        return currencyService.getHistory(request, pageable).stream().map(r -> toRateResponse(r, request.currency())).collect(Collectors.toList());
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<Map<String, String>> handleDuplicate(DuplicateRequestException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    private RateResponse toRateResponse(Rates rates, String currency) {
        return new RateResponse(
                currency,
                rates.getDate(),
                rates.getTimestamp() * 1000,
                rates.crossRatesFor(currency)
        );
    }
}
