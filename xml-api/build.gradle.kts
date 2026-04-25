plugins {
    java
    id("org.springframework.boot")
    id("io.freefair.lombok") version "9.2.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    implementation(project(":domain"))
    runtimeOnly("org.postgresql:postgresql")
}