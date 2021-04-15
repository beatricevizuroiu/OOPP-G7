plugins {
    id("io.freefair.lombok") version "6.0.0-m2"
}

repositories {
    mavenCentral()
}

val javaFXPlatform: String = getJFXPlatform()
val javaFXVersion: String = "15.0.1"

dependencies {
    testImplementation("org.mock-server:mockserver-netty:5.11.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    implementation("org.openjfx:javafx-base:${javaFXVersion}:${javaFXPlatform}")
    implementation("org.openjfx:javafx-graphics:${javaFXVersion}:${javaFXPlatform}")
    implementation("org.openjfx:javafx-controls:${javaFXVersion}:${javaFXPlatform}")
    implementation("org.openjfx:javafx-fxml:${javaFXVersion}:${javaFXPlatform}")


    implementation("com.google.code.gson:gson:2.8.6")
    implementation(project(":common"))
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "nl.tudelft.oopp.g7.client.Main"
    }

    // To add all of the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

fun getJFXPlatform(): String {
    val currentOs = org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem();
    if (currentOs.isWindows)
        return "win"

    if (currentOs.isLinux)
        return "linux"

    if (currentOs.isMacOsX)
        return "mac"

    return "";
}
