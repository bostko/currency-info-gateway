package net.isbg.currency.ratescollector;

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
    static void main(String[] args) {
        SpringApplication.run(RatesCollectorApplication.class, args);
    }
}
