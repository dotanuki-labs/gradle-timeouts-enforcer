package io.labs.dotanuki.timeouts_enforcer.util

import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.InvalidConfiguration
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.MissingConfiguration
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutsDefinition
import java.time.Duration
import java.time.temporal.ChronoUnit

class PluginConfigurationParser {

    fun parseFrom(properties: Map<String, Any?>): TimeoutsDefinition {
        val taskSpec = properties[perTask] as? String ?: throw MissingConfiguration(perTask)
        val buildSpec = properties[perBuild] as? String ?: throw MissingConfiguration(perBuild)
        return TimeoutsDefinition(parseDuration(taskSpec), parseDuration(buildSpec))
    }

    private fun parseDuration(durationSpec: String): Duration = try {
        val (amount, duration) = durationSpec.split(".").take(2)
        println("Amount -> $amount")
        println("Unit -> $duration")

        val unit = when (duration.toUpperCase()) {
            "SECONDS", "SECOND" -> ChronoUnit.SECONDS
            "MINUTES", "MINUTE" -> ChronoUnit.MINUTES
            "HOURS", "HOUR" -> ChronoUnit.HOURS
            else -> throw IllegalArgumentException("Unsupported unit")
        }

        Duration.of(amount.toLong(), unit)
    } catch (error: Throwable) {
        throw InvalidConfiguration(durationSpec)
    }

    companion object {
        const val perTask = "io.labs.dotanuki.enforcer.maxDurationPerTask"
        const val perBuild = "io.labs.dotanuki.enforcer.maxDurationPerBuild"
    }
}