package io.labs.dotanuki.timeouts_enforcer

import io.labs.dotanuki.timeouts_enforcer.TimeoutEnforcerException.BuildTimeoutReached
import io.labs.dotanuki.timeouts_enforcer.TimeoutEnforcerException.InvalidGradleVersion
import io.labs.dotanuki.timeouts_enforcer.TimeoutEnforcerException.UnsupportedGradleVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel.INFO
import java.time.Duration
import java.time.temporal.ChronoUnit

internal class TimeoutsEnforcerPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val buildTimeoutTracker = BuildTimeoutTracker(DEFAULT_BUILD_TIMEOUT).apply { start() }

        fun checkBuildTimeout() {
            if (buildTimeoutTracker.shouldAbort()) {
                throw BuildTimeoutReached(DEFAULT_BUILD_TIMEOUT)
            }
        }

        with(target.gradle) {
            ensureValidVersion(gradleVersion)

            taskGraph.whenReady { graph ->
                graph.allTasks.forEach { task ->
                    task.run {
                        evaluateAndSetTimeout(DEFAULT_TASK_TIMEOUT)
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
        val DEFAULT_BUILD_TIMEOUT: Duration = Duration.of(8L, ChronoUnit.SECONDS)
        val DEFAULT_TASK_TIMEOUT: Duration = Duration.of(5L, ChronoUnit.SECONDS)
        const val MINIMUM_MAJOR_VERSION = 5
        const val TIMEOUT_PROPERTY_NAME = "timeout"
    }
}