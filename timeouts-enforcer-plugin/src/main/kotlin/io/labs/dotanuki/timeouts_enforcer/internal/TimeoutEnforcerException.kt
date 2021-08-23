package io.labs.dotanuki.timeouts_enforcer.internal

import org.gradle.api.GradleException

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

    data class UnsupportedGradleVersion(
        val version: String
    ) : TimeoutEnforcerException("Unsupported Gradle version -> $version. Must be 5.x.y or newer")
}
