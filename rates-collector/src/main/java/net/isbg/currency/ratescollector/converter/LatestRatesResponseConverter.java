package net.isbg.currency.ratescollector.converter;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.ratescollector.dto.LatestRatesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LatestRatesResponseConverter {

    public Rates toRates(LatestRatesResponse response) {
        return new Rates(
                response.timestamp(),
                response.base(),
                LocalDate.parse(response.date()),
                response.rates()
        );
    }
}