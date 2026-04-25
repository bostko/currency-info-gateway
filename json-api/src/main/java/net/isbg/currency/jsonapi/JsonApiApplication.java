package net.isbg.currency.jsonapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "net.isbg.currency.jsonapi")
public class JsonApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonApiApplication.class, args);
    }
}