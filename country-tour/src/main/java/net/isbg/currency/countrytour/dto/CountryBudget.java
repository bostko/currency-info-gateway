package net.isbg.currency.countrytour.dto;

public record CountryBudget(
        String country,
        String currency,
        double amount
) {}