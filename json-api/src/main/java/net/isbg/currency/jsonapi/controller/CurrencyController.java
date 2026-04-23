package net.isbg.currency.jsonapi.controller;

import net.isbg.currency.jsonapi.dto.CurrencyRateResponse;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import net.isbg.currency.jsonapi.exception.DuplicateRequestException;
import net.isbg.currency.jsonapi.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/json_api")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/current")
    public CurrencyRateResponse current(@RequestBody CurrentRequest request) {
        return currencyService.getCurrent(request);
    }

    @PostMapping("/history")
    public List<CurrencyRateResponse> history(@RequestBody HistoryRequest request) {
        return currencyService.getHistory(request);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<Map<String, String>> handleDuplicate(DuplicateRequestException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
}
