@file:Suppress("UnstableApiUsage")

package io.labs.dotanuki.timeouts_enforcer.domain

import io.labs.dotanuki.timeouts_enforcer.util.formatMessage
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel.INFO
import java.time.Duration

internal object TaskPatcher {

    fun patchWithTimeout(target: Task, maxRunning: Duration) {
        target.run {
            timeout.orNull
                ?.let { logger.log(INFO, it.formatMessage("Detected predefined timeout for $name")) }
                ?: setProperty(TIMEOUT_PROPERTY_NAME, maxRunning)
        }
    }

    private const val TIMEOUT_PROPERTY_NAME = "timeout"
}