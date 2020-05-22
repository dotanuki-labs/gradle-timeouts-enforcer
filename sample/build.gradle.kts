import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("io.labs.dotanuki:timeouts-enforcer-plugin:+")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    application
}

apply(plugin = "io.labs.dotanuki.timeoutsenforcer")

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

    register("eureka") {

        doFirst {
            Thread.sleep(10000)
        }

        doLast {
            println("Eureka!")
        }
    }
}

application {
    mainClassName = "io.labs.dotanuki.sample.ApplicationKt"
}