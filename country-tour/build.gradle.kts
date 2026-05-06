plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation(project(":domain"))
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}

tasks.test {
    useJUnitPlatform()
}

val npmInstall = tasks.register<Exec>("npmInstall") {
    workingDir("frontend")
    commandLine("npm", "install")
    inputs.file("frontend/package.json")
    outputs.dir("frontend/node_modules")
}

val buildFrontend = tasks.register<Exec>("buildFrontend") {
    dependsOn(npmInstall)
    workingDir("frontend")
    commandLine("npm", "run", "build")
    inputs.dir("frontend/src")
    inputs.files("frontend/index.html", "frontend/vite.config.js", "frontend/package.json")
    outputs.dir(layout.projectDirectory.dir("src/main/resources/static"))
}

tasks.processResources {
    dependsOn(buildFrontend)
}