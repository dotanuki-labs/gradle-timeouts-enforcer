import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `application`
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("io.labs.dotanuki.timeoutsenforcer") version "0.0.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.71")
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }
}

application {
    mainClassName = "io.labs.dotanuki.sample.ApplicationKt"
}