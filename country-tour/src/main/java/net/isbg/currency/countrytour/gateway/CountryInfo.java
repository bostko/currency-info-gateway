package net.isbg.currency.countrytour.gateway;

import java.util.List;
import java.util.Map;

public record CountryInfo(
        String cca2,
        List<String> borders,
        Map<String, CurrencyDetail> currencies
) {
    public record CurrencyDetail(String name, String symbol) {}

    public String primaryCurrencyCode() {
        if (currencies == null || currencies.isEmpty()) return null;
        return currencies.keySet().iterator().next();
    }
}