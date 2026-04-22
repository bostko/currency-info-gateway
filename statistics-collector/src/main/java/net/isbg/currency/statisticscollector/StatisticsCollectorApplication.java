package net.isbg.currency.statisticscollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StatisticsCollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsCollectorApplication.class, args);
    }

}