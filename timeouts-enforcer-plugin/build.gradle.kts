import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    id("idea")
    id("org.gradle.java-gradle-plugin")
    id("org.gradle.maven-publish")
    id("com.adarshr.test-logger") version "3.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("com.gradle.plugin-publish") version "0.15.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val releaseVersion: String = properties["tag"]?.toString()?.ifEmpty { "SNAPSHOT" } ?: "SNAPSHOT"

group = "io.labs.dotanuki"
version = releaseVersion

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = sourceCompatibility
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("com.google.testparameterinjector:test-parameter-injector:1.4")
    testImplementation(gradleTestKit())
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test>().configureEach {
        dependsOn("publishToMavenLocal")
    }
}

val sourcesJar = tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    val localRepoPath = "${rootProject.rootDir}/repo"
    repositories {
        maven(url = uri(localRepoPath))
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}

gradlePlugin {
    plugins {
        create("gradle-timeouts-enforcer") {
            id = "io.labs.dotanuki.timeoutsenforcer"
            displayName = "Gradle Timeouts Enforcer Plugin"
            description = "Ensure that no tasks in your Gradle build take more time they should"
            implementationClass = "io.labs.dotanuki.timeouts_enforcer.TimeoutsEnforcerPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/dotanuki-labs/gradle-timeouts-enforcer"
    vcsUrl = "https://github.com/dotanuki-labs/gradle-timeouts-enforcer"
    description = "Ensures that your Gradle build never runs forever, for whatever reason"

    (plugins) {
        "gradle-timeouts-enforcer" {
            displayName = "Gradle Timeouts Enforcer Plugin"
            tags = listOf("timeout", "timeouts", "kotlin-dsl", "plugin")
            version = releaseVersion
        }
    }
}

testlogger {
    theme = MOCHA
    showStandardStreams = false
    showFailedStandardStreams = true
    showExceptions = true
    showFullStackTraces = true
}
