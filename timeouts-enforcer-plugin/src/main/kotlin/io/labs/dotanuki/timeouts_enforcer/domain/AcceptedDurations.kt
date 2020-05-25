package io.labs.dotanuki.timeouts_enforcer.domain

import java.time.Duration

internal data class AcceptedDurations(
    val perTask: Duration,
    val perBuild: Duration
)