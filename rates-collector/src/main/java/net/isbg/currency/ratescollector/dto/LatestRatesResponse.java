package net.isbg.currency.ratescollector.dto;

import java.util.Map;

public record LatestRatesResponse(
        boolean success,
        long timestamp,
        String base,
        String date,
        Map<String, Double> rates,
        ErrorInfo error
) {
    public record ErrorInfo(int code, String type, String info) {}
}
