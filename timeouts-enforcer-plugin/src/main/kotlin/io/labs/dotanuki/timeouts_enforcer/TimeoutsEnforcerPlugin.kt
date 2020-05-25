package io.labs.dotanuki.timeouts_enforcer

import io.labs.dotanuki.timeouts_enforcer.domain.BuildTimeoutTracker
import io.labs.dotanuki.timeouts_enforcer.domain.GradleVersionChecker
import io.labs.dotanuki.timeouts_enforcer.domain.PluginConfigurationParser
import io.labs.dotanuki.timeouts_enforcer.domain.TaskPatcher
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal class TimeoutsEnforcerPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val acceptedDurations = PluginConfigurationParser().parseFrom(target.properties)
        val globalTracker = BuildTimeoutTracker(acceptedDurations.perBuild)

        globalTracker.start()

        with(target.gradle) {
            GradleVersionChecker.requireValidVersion(gradleVersion)

            taskGraph.whenReady { graph ->
                graph.allTasks.forEach { task ->
                    task.run {
                        TaskPatcher.patchWithTimeout(task, acceptedDurations.perTask)
                        doFirst { globalTracker.abortIfNeeded() }
                        doLast { globalTracker.abortIfNeeded() }
                    }
                }
            }
        }
    }
}