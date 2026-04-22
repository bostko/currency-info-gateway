package net.isbg.currency.jsonapi.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.cfg.DateTimeFeature;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer instantAsMillis() {
        return builder -> builder
                .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(
                        DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
                        DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS
                );
    }
}
