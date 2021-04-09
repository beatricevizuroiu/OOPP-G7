plugins {
    id("org.openjfx.javafxplugin") version "0.0.9"
    id("io.freefair.lombok") version "5.3.0"
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.mock-server:mockserver-netty:5.11.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation(project(":common"))
}

javafx {
    version = "15"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "nl.tudelft.oopp.g7.client.EntryApp"
    }

    // To add all of the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
