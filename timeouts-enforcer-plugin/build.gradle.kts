import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    with(BuildPlugins) {
        id(kotlinJVM)
        id(ktlint)
        id(testLogger)
        id(gradlePluginDevelopment)
        id(mavenPublisher)
        id(gradlePluginPublisher) version Versions.gradlePublishPlugin
    }
}

dependencies {
    with(Libraries) {
        implementation(kotlinStdLib)
        implementation(kotlinCoroutines)
        testImplementation(junit)
        testImplementation(assertj)
        testImplementation(gradleTestKit())
    }
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = Definitions.targetJDK
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
            displayName = "Timeouts Enforcer Gradle Plugin"
            description = "Ensure that no tasks in your Gradle build take more time they should"
            implementationClass = "io.labs.dotanuki.timeouts_enforcer.TimeoutsEnforcerPlugin"
        }
    }
}