package net.isbg.currency.countrytour.controller;

import net.isbg.currency.countrytour.dto.TourRequest;
import net.isbg.currency.countrytour.dto.TourResponse;
import net.isbg.currency.countrytour.service.TourCalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tour")
public class TourController {

    private final TourCalculationService tourCalculationService;

    public TourController(TourCalculationService tourCalculationService) {
        this.tourCalculationService = tourCalculationService;
    }

    @GetMapping
    public TourResponse tour(
            @RequestParam String country,
            @RequestParam double budgetPerCountry,
            @RequestParam double totalBudget,
            @RequestParam String currency) {
        return tourCalculationService.calculate(
                new TourRequest(country, budgetPerCountry, totalBudget, currency)
        );
    }
}