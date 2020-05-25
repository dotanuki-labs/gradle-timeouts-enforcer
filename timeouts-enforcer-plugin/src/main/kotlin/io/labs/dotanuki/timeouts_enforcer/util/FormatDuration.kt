package io.labs.dotanuki.timeouts_enforcer.util

import java.time.Duration

internal fun Duration.formatMessage(message: String): String =
    "$message -> $seconds seconds (${toMinutes()} minutes)"