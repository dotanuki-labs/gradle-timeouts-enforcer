package io.labs.dotanuki.timeouts_enforcer.internal

import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.InvalidGradleVersion
import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.UnsupportedGradleVersion

internal object GradleVersionChecker {

    fun requireValidVersion(gradleVersion: String) {
        gradleVersion.split(".").firstOrNull()
            ?.let { if (unsupported(it, gradleVersion)) throw UnsupportedGradleVersion(gradleVersion) }
            ?: throw InvalidGradleVersion(gradleVersion)
    }

    private fun unsupported(major: String, gradleVersion: String): Boolean =
        try {
            major.toInt() < MINIMUM_MAJOR_VERSION
        } catch (error: Throwable) {
            throw InvalidGradleVersion(gradleVersion)
        }

    private const val MINIMUM_MAJOR_VERSION = 5
}