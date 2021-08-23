package io.labs.dotanuki.timeouts_enforcer

import io.labs.dotanuki.timeouts_enforcer.internal.GradleVersionChecker
import io.labs.dotanuki.timeouts_enforcer.internal.ProjectPropertiesParser
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class TimeoutsEnforcerPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val userDefinedTimeout = ProjectPropertiesParser.tasksTimeout(target.properties)

        with(target.gradle) {
            GradleVersionChecker.requireValidVersion(gradleVersion)

            taskGraph.whenReady { graph ->
                graph.allTasks.forEach { task ->
                    task.timeout.set(userDefinedTimeout.value)
                }
            }
        }
    }
}