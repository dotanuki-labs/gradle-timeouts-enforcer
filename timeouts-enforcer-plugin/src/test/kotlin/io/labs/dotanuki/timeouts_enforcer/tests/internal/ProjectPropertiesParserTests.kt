package io.labs.dotanuki.timeouts_enforcer.tests.internal

import io.labs.dotanuki.timeouts_enforcer.internal.ProjectPropertiesParser
import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.InvalidConfiguration
import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.MissingConfiguration
import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutForTasks
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ThrowableAssert
import org.junit.Test
import java.time.Duration
import java.time.temporal.ChronoUnit

class ProjectPropertiesParserTests {

    @Test fun `should fail when missing property`() {
        val broken = mapOf(
            "org.gradle.parallel" to "true"
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.tasksTimeout(broken)
        }

        val expected = MissingConfiguration(ProjectPropertiesParser.DURATION_PER_TASK)

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should fail when property is unparseable`() {
        val broken = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "10_minutes",
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.tasksTimeout(broken)
        }

        val expected = InvalidConfiguration("10_minutes")

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should fail when chrono unit not supported`() {
        val broken = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "1.day",
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.tasksTimeout(broken)
        }

        val expected = InvalidConfiguration("1.day")

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should fail when time amount not supported`() {
        val broken = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "1,5.minutes",
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.tasksTimeout(broken)
        }

        val expected = InvalidConfiguration("1,5.minutes")

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should parse valid properties`() {
        val valid = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "15.minutes",
        )

        val extracted = ProjectPropertiesParser.tasksTimeout(valid)

        val expected = TimeoutForTasks(
            Duration.of(15L, ChronoUnit.MINUTES),
        )

        assertThat(extracted).isEqualTo(expected)
    }
}