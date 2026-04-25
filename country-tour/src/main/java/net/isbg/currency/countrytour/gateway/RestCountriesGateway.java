package net.isbg.currency.countrytour.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
        List<CountryInfo> result = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryInfo>>() {}
        ).getBody();
        if (result == null || result.isEmpty()) {
            throw new IllegalArgumentException("Country not found: " + countryCode);
        }
        return result.get(0);
    }
}