package net.isbg.currency.countrytour.dto;

public record TourRequest(
        String country,
        double budgetPerCountry,
        double totalBudget,
        String currency
) {}