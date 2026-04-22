package net.isbg.currency.ratescollector;

import net.isbg.currency.ratescollector.dto.LatestRatesResponse;
import net.isbg.currency.ratescollector.gateway.FixerIoGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Instant;

@SpringBootApplication
@EnableScheduling
@EntityScan("net.isbg.currency.domain.entity")
@EnableJpaRepositories("net.isbg.currency.domain.repository")
public class RatesCollectorApplication {
    private static final Logger log = LoggerFactory.getLogger(RatesCollectorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RatesCollectorApplication.class, args);
    }

    @Bean
    public CommandLineRunner fetchLatestRatesOnStartup(FixerIoGateway fixerIoGateway) {
        return args -> {
            log.info("Fetching latest exchange rates from fixer.io...");
            LatestRatesResponse response = fixerIoGateway.getLatest();
            if (response.success()) {
                log.info("Latest rates fetched for timestamp instant  '{}' on {}: {}", Instant.ofEpochSecond(response.timestamp()));
                log.info("Latest rates fetched for base '{}' ", response);
            } else {
                log.warn("Failed to fetch rates: {}", response.error() != null ? response.error().info() : "unknown error");
            }
        };
    }
}