
plugins {
    id("org.gradle.java-gradle-plugin")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    group = "io.labs.dotanuki"
    version = "0.1.0"
}
