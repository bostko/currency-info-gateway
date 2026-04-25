package net.isbg.currency.jsonapi.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("net.isbg.currency.domain.entity")
@EnableJpaRepositories("net.isbg.currency.domain.repository")
public class JpaConfig {
}