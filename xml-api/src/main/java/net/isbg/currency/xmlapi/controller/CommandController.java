package net.isbg.currency.xmlapi.controller;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.xmlapi.dto.CommandRequest;
import net.isbg.currency.xmlapi.dto.CommandResponse;
import net.isbg.currency.xmlapi.dto.RateResponse;
import net.isbg.currency.xmlapi.service.AuditService;
import net.isbg.currency.xmlapi.service.CurrencyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/xml_api")
public class CommandController {

    private final CurrencyService currencyService;
    private final AuditService auditService;

    public CommandController(CurrencyService currencyService, AuditService auditService) {
        this.currencyService = currencyService;
        this.auditService = auditService;
    }

    @PostMapping(value = "/command",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public CommandResponse command(@RequestBody CommandRequest request) {
        if (request.getGet() != null) {
            auditService.auditCurrent(request);
            Rates rates = currencyService.getCurrent(request.getGet().getCurrency());
            return new CommandResponse(
                    request.getRequestId(),
                    request.getGet().getConsumer(),
                    List.of(toRateResponse(rates, request.getGet().getCurrency()))
            );
        } else {
            auditService.auditHistory(request);
            List<RateResponse> rateResponses = currencyService
                    .getHistory(request.getHistory().getCurrency(), request.getHistory().getPeriod())
                    .stream()
                    .map(r -> toRateResponse(r, request.getHistory().getCurrency()))
                    .toList();
            return new CommandResponse(
                    request.getRequestId(),
                    request.getHistory().getConsumer(),
                    rateResponses
            );
        }
    }

    private RateResponse toRateResponse(Rates rates, String currency) {
        return new RateResponse(
                currency,
                rates.getRates().get(currency),
                rates.getBase(),
                rates.getDate().toString(),
                rates.getTimestamp() * 1000
        );
    }
}