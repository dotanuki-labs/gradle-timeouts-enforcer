package io.labs.dotanuki.timeouts_enforcer

import java.time.Duration
import kotlin.concurrent.thread

internal class BuildTimeoutTracker(duration: Duration) {

    private var hasTimedOut: Boolean = false

    private val watcher by lazy {
        thread(name = "global-timeout", start = false, isDaemon = false) {
            Thread.sleep(duration.toMillis())
            hasTimedOut = true
        }
    }

    fun shouldAbort(): Boolean = hasTimedOut

    fun start() {
        watcher.run {
            start()
            join()
        }
    }
}