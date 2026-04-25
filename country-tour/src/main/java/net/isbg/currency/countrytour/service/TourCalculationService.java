package net.isbg.currency.countrytour.service;

import net.isbg.currency.countrytour.dto.CountryBudget;
import net.isbg.currency.countrytour.dto.TourRequest;
import net.isbg.currency.countrytour.dto.TourResponse;
import net.isbg.currency.countrytour.gateway.CountryInfo;
import net.isbg.currency.countrytour.gateway.RestCountriesGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourCalculationService {

    private final RestCountriesGateway restCountriesGateway;
    private final ExchangeRateService exchangeRateService;

    public TourCalculationService(RestCountriesGateway restCountriesGateway,
                                  ExchangeRateService exchangeRateService) {
        this.restCountriesGateway = restCountriesGateway;
        this.exchangeRateService = exchangeRateService;
    }

    public TourResponse calculate(TourRequest request) {
        CountryInfo startingCountry = restCountriesGateway.getByCode(request.country());
        List<String> borders = startingCountry.borders();

        if (borders == null || borders.isEmpty()) {
            return new TourResponse(
                    request.country(), request.currency(),
                    request.totalBudget(), request.budgetPerCountry(),
                    0, 0, request.totalBudget(), List.of()
            );
        }

        int neighborCount = borders.size();
        double costPerTour = request.budgetPerCountry() * neighborCount;
        int completeTours = (int) (request.totalBudget() / costPerTour);
        double leftover = request.totalBudget() - (completeTours * costPerTour);

        List<CountryBudget> countryBudgets = borders.stream()
                .map(borderCode -> {
                    CountryInfo neighbor = restCountriesGateway.getByCode(borderCode);
                    String targetCurrency = neighbor.primaryCurrencyCode();
                    double localAmount = targetCurrency != null
                            ? exchangeRateService.convert(request.budgetPerCountry(), request.currency(), targetCurrency)
                            : request.budgetPerCountry();
                    String displayCurrency = targetCurrency != null ? targetCurrency : request.currency();
                    return new CountryBudget(borderCode, displayCurrency, localAmount);
                })
                .toList();

        return new TourResponse(
                request.country(),
                request.currency(),
                request.totalBudget(),
                request.budgetPerCountry(),
                neighborCount,
                completeTours,
                leftover,
                countryBudgets
        );
    }
}