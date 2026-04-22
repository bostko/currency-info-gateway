plugins {
    java
    id("org.springframework.boot")
    id("io.freefair.lombok") version "9.2.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
