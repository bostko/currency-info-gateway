package net.isbg.currency.countrytour.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCountriesGateway {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RestCountriesGateway(
            RestTemplate restTemplate,
            @Value("${country-tour.rest-countries-base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Cacheable("countries")
    public CountryInfo getByCode(String countryCode) {
        String url = baseUrl + "/alpha/" + countryCode + "?fields=cca2,borders,currencies";
        CountryInfo result = restTemplate.getForObject(url, CountryInfo.class);
        if (result == null) {
            throw new IllegalArgumentException("Country not found: " + countryCode);
        }
        return result;
    }
}