package net.isbg.currency.jsonapi.dto;

import java.math.BigDecimal;

public record CurrencyRateResponse(String currency, BigDecimal rate, long timestamp) {}