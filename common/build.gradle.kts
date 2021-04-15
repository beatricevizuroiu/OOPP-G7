plugins {
    id("io.freefair.lombok") version "6.0.0-m2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("javax.validation:validation-api:2.0.1.Final")
}

java {
    modularity.inferModulePath.set(true)
}

plugins.withType<JavaPlugin>().configureEach {
    configure<JavaPluginExtension> {
        modularity.inferModulePath.set(true)
    }
}
