pluginManagement {
    plugins {
        id("org.springframework.boot") version "4.0.3"
        id("io.spring.dependency-management") version "1.1.7"
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "currency-info-gateway"
include("xml-api", "json-api", "domain", "rates-collector", "statistics-collector", "service")
