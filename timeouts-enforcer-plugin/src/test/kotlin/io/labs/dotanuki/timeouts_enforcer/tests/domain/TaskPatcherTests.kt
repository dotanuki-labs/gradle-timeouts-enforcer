package io.labs.dotanuki.timeouts_enforcer.tests.domain

import io.labs.dotanuki.timeouts_enforcer.domain.TaskPatcher
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.temporal.ChronoUnit

class TaskPatcherTests {

    open class CustomGradleTask : DefaultTask()

    private lateinit var task: Task

    @Before fun `before each test`() {
        val project = ProjectBuilder.builder().build()
        task = project.tasks.create("test", CustomGradleTask::class.java)
    }

    @Test fun `should patch task without predefined timeout`() {
        val duration = Duration.of(10, ChronoUnit.MINUTES)

        TaskPatcher.patchWithTimeout(task, duration)

        assertThat(task.timeout.get()).isEqualTo(duration)
    }

    @Test fun `should skip patching when task has predefined timeout`() {
        val predefined = Duration.of(5, ChronoUnit.MINUTES)
        task.run { setProperty("timeout", predefined) }

        val duration = Duration.of(10, ChronoUnit.MINUTES)

        TaskPatcher.patchWithTimeout(task, duration)

        assertThat(task.timeout.get()).isEqualTo(predefined)
    }
}