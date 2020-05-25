package io.labs.dotanuki.timeouts_enforcer.domain

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration

internal class BuildTimeoutTracker(private val duration: Duration) {

    private var hasTimedOut: Boolean = false

    fun abortIfNeeded() {
        if (hasTimedOut) throw TimeoutEnforcerException.BuildTimeoutReached(duration)
    }

    fun start() {
        hasTimedOut = false

        GlobalScope.launch {
            delay(duration.toMillis())
            hasTimedOut = true
        }
    }
}