package io.labs.dotanuki.timeouts_enforcer.domain

import java.time.Duration
import java.time.temporal.ChronoUnit

data class TimeoutsDefinition(
    val perTask: Duration = Duration.of(30, ChronoUnit.MINUTES),
    val perBuild: Duration = Duration.of(2, ChronoUnit.HOURS)
)