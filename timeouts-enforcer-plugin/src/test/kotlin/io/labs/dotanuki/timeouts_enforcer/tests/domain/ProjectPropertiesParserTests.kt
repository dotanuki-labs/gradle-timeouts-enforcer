package io.labs.dotanuki.timeouts_enforcer.tests.domain

import io.labs.dotanuki.timeouts_enforcer.domain.AcceptedDurations
import io.labs.dotanuki.timeouts_enforcer.domain.ProjectPropertiesParser
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.InvalidConfiguration
import io.labs.dotanuki.timeouts_enforcer.domain.TimeoutEnforcerException.MissingConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ThrowableAssert
import org.junit.Test
import java.time.Duration
import java.time.temporal.ChronoUnit

class ProjectPropertiesParserTests {

    @Test fun `should fail when missing property`() {
        val broken = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "10 minutes"
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.extractDurations(broken)
        }

        val expected = MissingConfiguration(ProjectPropertiesParser.DURATION_PER_BUILD)

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should fail when property is unparseable`() {
        val broken = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "10.minutes",
            ProjectPropertiesParser.DURATION_PER_BUILD to "10_HOURS"
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.extractDurations(broken)
        }

        val expected = InvalidConfiguration("10_HOURS")

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should fail when chrono unit not supported`() {
        val broken = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "10.minutes",
            ProjectPropertiesParser.DURATION_PER_BUILD to "1.day"
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.extractDurations(broken)
        }

        val expected = InvalidConfiguration("1.day")

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should fail when time amount not supported`() {
        val broken = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "1,5.minutes",
            ProjectPropertiesParser.DURATION_PER_BUILD to "1.day"
        )

        val execution = ThrowableAssert.ThrowingCallable {
            ProjectPropertiesParser.extractDurations(broken)
        }

        val expected = InvalidConfiguration("1,5.minutes")

        assertThatThrownBy(execution).isEqualTo(expected)
    }

    @Test fun `should parse valid properties`() {
        val valid = mapOf(
            ProjectPropertiesParser.DURATION_PER_TASK to "15.minutes",
            ProjectPropertiesParser.DURATION_PER_BUILD to "2.hours"
        )

        val extracted = ProjectPropertiesParser.extractDurations(valid)

        val expected = AcceptedDurations(
            perTask = Duration.of(15L, ChronoUnit.MINUTES),
            perBuild = Duration.of(2L, ChronoUnit.HOURS)
        )

        assertThat(extracted).isEqualTo(expected)
    }
}