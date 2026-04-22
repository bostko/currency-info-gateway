package net.isbg.currency.jsonapi.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CurrencyRateResponse(String currency, BigDecimal rate, Instant timestamp) {}