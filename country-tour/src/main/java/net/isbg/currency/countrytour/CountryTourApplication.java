package net.isbg.currency.countrytour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("net.isbg.currency.domain.entity")
@EnableJpaRepositories("net.isbg.currency.domain.repository")
@EnableCaching
public class CountryTourApplication {

    public static void main(String[] args) {
        SpringApplication.run(CountryTourApplication.class, args);
    }
}