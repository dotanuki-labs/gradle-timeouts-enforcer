package io.labs.dotanuki.timeouts_enforcer.internal

import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.InvalidConfiguration
import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.MissingConfiguration
import java.time.Duration
import java.time.temporal.ChronoUnit

internal object ProjectPropertiesParser {

    fun tasksTimeout(properties: Map<String, Any?>): TimeoutForTasks {
        val spec = properties[DURATION_PER_TASK] as? String ?: throw MissingConfiguration(DURATION_PER_TASK)
        return TimeoutForTasks(parseDuration(spec))
    }

    private fun parseDuration(durationSpec: String): Duration = try {
        val (amount, duration) = durationSpec.split(".").take(2)
        Duration.of(amount.toLong(), parseChronoUnit(duration, durationSpec))
    } catch (error: Throwable) {
        throw InvalidConfiguration(durationSpec)
    }

    private fun parseChronoUnit(duration: String, durationSpec: String): ChronoUnit {
        val unit = when (duration.uppercase()) {
            in SPEC_SECONDS -> ChronoUnit.SECONDS
            in SPEC_MINUTES -> ChronoUnit.MINUTES
            in SPEC_HOURS -> ChronoUnit.HOURS
            else -> error("Unsupported spec unit -> $durationSpec")
        }
        return unit
    }

    const val DURATION_PER_TASK = "io.dotanuki.gradle.timeouts.tasks"

    private val SPEC_SECONDS = setOf("SECONDS", "SECOND")
    private val SPEC_MINUTES = setOf("MINUTES", "MINUTE")
    private val SPEC_HOURS = setOf("HOURS", "HOUR")
}