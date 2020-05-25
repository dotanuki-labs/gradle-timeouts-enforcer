package io.labs.dotanuki.timeouts_enforcer.domain

import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.InvalidGradleVersion

internal object GradleVersionChecker {

    fun requireValidVersion(gradleVersion: String) {
        gradleVersion.split(".").firstOrNull()
            ?.let { if (it.toInt() < MINIMUM_MAJOR_VERSION) throw InvalidGradleVersion(gradleVersion) }
            ?: throw TimeoutEnforcerException.UnsupportedGradleVersion(gradleVersion)
    }

    private const val MINIMUM_MAJOR_VERSION = 5
}