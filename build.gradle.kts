plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
}

subprojects {
    repositories {
        mavenCentral()
    }

    plugins.withId("java") {
        the<JavaPluginExtension>().toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    apply(plugin = "io.spring.dependency-management")
    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }
}
