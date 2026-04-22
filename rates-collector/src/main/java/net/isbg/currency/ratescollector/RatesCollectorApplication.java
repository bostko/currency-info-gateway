package net.isbg.currency.ratescollector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("net.isbg.currency.domain.entity")
@EnableJpaRepositories("net.isbg.currency.domain.repository")
public class RatesCollectorApplication {
    private static final Logger log = LoggerFactory.getLogger(RatesCollectorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RatesCollectorApplication.class, args);
    }
}