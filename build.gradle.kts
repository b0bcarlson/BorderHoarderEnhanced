import java.net.URI
plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.simondmc"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = URI.create("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "${group}.borderhoarder"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar")}.map {zipTree(it)}
    })
}