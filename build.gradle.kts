import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    application

    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.malcrove"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation ("org.bitcoinj", "bitcoinj-core", "0.16.1")
    implementation ("org.slf4j", "slf4j-api", "1.7.36")
    implementation ("org.slf4j", "slf4j-simple", "1.7.36")
    implementation ("me.tongfei", "progressbar", "0.9.3")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "Main"))
        }
    }
}

application {
    mainClassName = "MainKt"
}