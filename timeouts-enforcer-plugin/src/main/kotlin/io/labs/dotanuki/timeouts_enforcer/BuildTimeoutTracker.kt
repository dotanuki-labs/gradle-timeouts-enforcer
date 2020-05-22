package io.labs.dotanuki.timeouts_enforcer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration

internal class BuildTimeoutTracker(private val duration: Duration) {

    private var hasTimedOut: Boolean = false

    fun shouldAbort(): Boolean = hasTimedOut

    fun start() {
        GlobalScope.launch {
            delay(duration.toMillis())
            hasTimedOut = true
        }
    }
}