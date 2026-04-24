package net.isbg.currency.jsonapi.dto;

import java.time.LocalDate;

public record RateResponse(
        String currency,
        double rate,
        String base,
        LocalDate date,
        long timestamp
) {}