package io.labs.dotanuki.timeouts_enforcer

import org.gradle.api.GradleException
import java.time.Duration

internal sealed class TimeoutEnforcerException(error: String) : GradleException(error) {

    data class BuildTimeoutReached(
        val timeout: Duration
    ) : TimeoutEnforcerException(
        timeout.formatMessage("Your build timed out")
    )

    data class InvalidGradleVersion(
        val version: String
    ) : TimeoutEnforcerException("Invalid Gradle version -> $version")

    data class UnsupportedGradleVersion(
        val version: String
    ) : TimeoutEnforcerException("Unsupported Gradle version -> $version. Must be 5.x.y or newer")
}