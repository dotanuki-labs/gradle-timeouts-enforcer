import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(BuildPlugins.kotlinJVM)
    application
}

dependencies {
    implementation(Dependencies.kotlinStdLib)
    implementation(project(":library"))
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = Definitions.targetJDK
    }
}

application {
    mainClassName = "io.labs.dotanuki.sample.ApplicationKt"
}