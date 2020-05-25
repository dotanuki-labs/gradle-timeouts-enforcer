package io.labs.dotanuki.timeouts_enforcer.domain

import io.labs.dotanuki.timeouts_enforcer.util.formatMessage
import org.gradle.api.GradleException
import java.time.Duration

internal sealed class TimeoutEnforcerException(error: String) : GradleException(error) {

    data class MissingConfiguration(
        val propertyName: String
    ) : TimeoutEnforcerException(
        "Missing mandatory property -> $propertyName"
    )

    data class InvalidConfiguration(
        val propertyName: String
    ) : TimeoutEnforcerException(
        "Cannot parse specification -> $propertyName"
    )

    data class InvalidGradleVersion(
        val version: String
    ) : TimeoutEnforcerException("Invalid Gradle version -> $version")

    data class BuildTimeoutReached(
        val timeout: Duration
    ) : TimeoutEnforcerException(
        timeout.formatMessage("Your build timed out after")
    )

    data class UnsupportedGradleVersion(
        val version: String
    ) : TimeoutEnforcerException("Unsupported Gradle version -> $version. Must be 5.x.y or newer")
}