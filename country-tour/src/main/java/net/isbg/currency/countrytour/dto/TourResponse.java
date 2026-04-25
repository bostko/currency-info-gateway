package net.isbg.currency.countrytour.dto;

import java.util.List;

public record TourResponse(
        String startingCountry,
        String currency,
        double totalBudget,
        double budgetPerCountry,
        int neighborCount,
        int completeTours,
        double leftover,
        List<CountryBudget> countryBudgets
) {}