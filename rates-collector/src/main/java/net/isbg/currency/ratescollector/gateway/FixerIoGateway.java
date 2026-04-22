package net.isbg.currency.ratescollector.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.isbg.currency.ratescollector.dto.LatestRatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FixerIoGateway {

    private static final Logger log = LoggerFactory.getLogger(FixerIoGateway.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UriComponentsBuilder baseUri;
    private final boolean mock;

    public FixerIoGateway(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${net.isbg.currency.collector.fixer_io_api_endpoint}") String apiEndpoint,
            @Value("${net.isbg.currency.collector.fixer_io_api_key}") String apiKey,
            @Value("${net.isbg.currency.collector.fixer_io_mock:false}") boolean mock) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.baseUri = UriComponentsBuilder
                .fromUriString(apiEndpoint)
                .queryParam("access_key", apiKey);
        this.mock = mock;
    }

    public LatestRatesResponse getLatest() throws Exception {
        if (mock) {
            log.debug("Mock enabled — loading response from mock-latest-response.json");
            return objectMapper.readValue(
                    new ClassPathResource("mock-latest-response.json").getInputStream(),
                    LatestRatesResponse.class);
        }

        String url = uriBuilder()
                .pathSegment("latest")
                .queryParam("base", "EUR")
                .toUriString();

        String raw = restTemplate.getForObject(url, String.class);
        log.debug("Raw response from /latest: {}", raw);
        return objectMapper.readValue(raw, LatestRatesResponse.class);
    }

    private UriComponentsBuilder uriBuilder() {
        return baseUri.cloneBuilder();
    }
}