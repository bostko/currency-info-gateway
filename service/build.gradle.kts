plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation(project(":domain"))
}