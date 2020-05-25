package io.labs.dotanuki.timeouts_enforcer

import io.labs.dotanuki.timeouts_enforcer.domain.BuildTimeoutTracker
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.BuildTimeoutReached
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.InvalidGradleVersion
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.UnsupportedGradleVersion
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutsDefinition
import io.labs.dotanuki.timeouts_enforcer.util.formatMessage
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel.INFO
import java.time.Duration

@Suppress("UnstableApiUsage")
internal class TimeoutsEnforcerPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val timeouts = TimeoutsDefinition()
        val buildTimeoutTracker = BuildTimeoutTracker(timeouts.perBuild)

        fun checkBuildTimeout() {
            if (buildTimeoutTracker.shouldAbort()) throw BuildTimeoutReached(timeouts.perBuild)
        }

        buildTimeoutTracker.start()

        with(target.gradle) {

            ensureValidVersion(gradleVersion)

            taskGraph.whenReady { graph ->

                graph.allTasks.forEach { task ->
                    task.run {
                        evaluateAndSetTimeout(timeouts.perTask)
                        doFirst { checkBuildTimeout() }
                        doLast { checkBuildTimeout() }
                    }
                }
            }
        }
    }

    private fun ensureValidVersion(gradleVersion: String) {
        gradleVersion.split(".").firstOrNull()
            ?.let { if (it.toInt() < MINIMUM_MAJOR_VERSION) throw InvalidGradleVersion(gradleVersion) }
            ?: throw UnsupportedGradleVersion(gradleVersion)
    }

    private fun Task.evaluateAndSetTimeout(taskTimeout: Duration) {
        timeout.orNull
            ?.let { logger.log(INFO, it.formatMessage("Detected predefined timeout for $name")) }
            ?: setProperty(TIMEOUT_PROPERTY_NAME, taskTimeout)
    }

    companion object {
        const val MINIMUM_MAJOR_VERSION = 5
        const val TIMEOUT_PROPERTY_NAME = "timeout"
    }
}