package io.labs.dotanuki.timeouts_enforcer.tests.internal

import io.labs.dotanuki.timeouts_enforcer.internal.GradleVersionChecker
import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.InvalidGradleVersion
import io.labs.dotanuki.timeouts_enforcer.internal.TimeoutEnforcerException.UnsupportedGradleVersion
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class GradleVersionCheckerTests {

    @Test fun `should catch invalid gradle versions`() {

        val table = mapOf(
            "3.4.1" to UnsupportedGradleVersion("3.4.1"),
            "alpha.0.0.1" to InvalidGradleVersion("alpha.0.0.1")
        )

        table.forEach { (version, expectedError) ->
            val execution = { GradleVersionChecker.requireValidVersion(version) }
            assertThatThrownBy(execution).isEqualTo(expectedError)
        }
    }

    @Test fun `should bypass on valid gradle version`() {
        val execution = { GradleVersionChecker.requireValidVersion("6.4.1") }
        assertThatCode(execution).doesNotThrowAnyException()
    }
}
