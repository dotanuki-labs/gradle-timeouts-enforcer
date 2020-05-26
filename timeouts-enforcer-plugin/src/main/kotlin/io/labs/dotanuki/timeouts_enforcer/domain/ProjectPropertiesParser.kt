package io.labs.dotanuki.timeouts_enforcer.domain

import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.InvalidConfiguration
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.MissingConfiguration
import java.time.Duration
import java.time.temporal.ChronoUnit

internal object ProjectPropertiesParser {

    fun extractDurations(properties: Map<String, Any?>): AcceptedDurations {
        val taskSpec = safeExtractProperty(properties, DURATION_PER_TASK)
        val buildSpec = safeExtractProperty(properties, DURATION_PER_BUILD)
        return AcceptedDurations(parseDuration(taskSpec), parseDuration(buildSpec))
    }

    private fun safeExtractProperty(properties: Map<String, Any?>, propertyName: String) =
        properties[propertyName] as? String ?: throw MissingConfiguration(propertyName)

    private fun parseDuration(durationSpec: String): Duration = try {
        val (amount, duration) = durationSpec.split(".").take(2)

        val unit = when (duration.toUpperCase()) {
            in SPEC_SECONDS -> ChronoUnit.SECONDS
            in SPEC_MINUTES -> ChronoUnit.MINUTES
            in SPEC_HOURS -> ChronoUnit.HOURS
            else -> error("Unsupported spec unit -> $durationSpec")
        }

        Duration.of(amount.toLong(), unit)
    } catch (error: Throwable) {
        throw InvalidConfiguration(durationSpec)
    }

    const val DURATION_PER_TASK = "io.labs.dotanuki.enforcer.maxDurationPerTask"
    const val DURATION_PER_BUILD = "io.labs.dotanuki.enforcer.maxDurationPerBuild"

    private val SPEC_SECONDS = setOf("SECONDS", "SECOND")
    private val SPEC_MINUTES = setOf("MINUTES", "MINUTE")
    private val SPEC_HOURS = setOf("HOURS", "HOUR")
}