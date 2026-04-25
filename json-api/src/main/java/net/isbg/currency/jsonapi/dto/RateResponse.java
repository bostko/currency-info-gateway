package net.isbg.currency.jsonapi.dto;

import java.time.LocalDate;
import java.util.Map;

public record RateResponse(
        String currency,
        LocalDate date,
        long timestamp,
        Map<String, Double> rates
) {}