package io.labs.dotanuki.timeouts_enforcer.tests

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import java.io.File

@RunWith(TestParameterInjector::class)
class TimeoutsEnforcerPluginTests {

    @get:Rule val tempFolder = TemporaryFolder()

    @Suppress("unused")
    enum class GradleTestVersion(val value: String) {
        FIVE("5.5"),
        SIX("6.8"),
        SEVEN("7.1"),
    }

    @Test fun `should not timeout if not needed`() {

        val fixtureDir = prepareFixture(perTaskSpec = "5.minutes")
        val millisToDelay = 100

        val build = GradleRunner.create()
            .withProjectDir(fixtureDir)
            .withPluginClasspath()
            .withGradleVersion("5.2.1")
            .withArguments("clean", "run", "--args='$millisToDelay'")
            .build()

        assertThat(build.output).contains("BUILD SUCCESSFUL")
    }

    @Test fun `should break project with unsupported Gradle version`() {

        val millisToDelay = 100
        val fixtureDir = prepareFixture(perTaskSpec = "5.minutes")

        val build = GradleRunner.create()
            .withProjectDir(fixtureDir)
            .withPluginClasspath()
            .withGradleVersion("4.10.3")
            .withArguments("clean", "run", "--args='$millisToDelay'")
            .buildAndFail()

        assertThat(build.output).apply {
            contains("Unsupported Gradle version")
            contains("BUILD FAILED")
        }
    }

    @Test fun `should timeout with slow task`(@TestParameter version: GradleTestVersion) {
        val fixtureDir = prepareFixture(perTaskSpec = "5.seconds")
        val millisToDelay = 10000

        val build = GradleRunner.create()
            .withProjectDir(fixtureDir)
            .withPluginClasspath()
            .withGradleVersion(version.value)
            .withArguments("clean", "run", "--args='$millisToDelay'")
            .buildAndFail()

        assertThat(build.output).apply {
            contains("Timeout has been exceeded")
            contains("BUILD FAILED")
        }
    }

    private fun prepareFixture(perTaskSpec: String): File =
        tempFolder.newFolder().apply {
            File("$TEST_FIXTURES/demo").copyRecursively(this)
            File("$this/gradle.properties").writeText(
                """
                io.dotanuki.gradle.timeouts.tasks=$perTaskSpec
                """.trimIndent()
            )
        }

    companion object {
        private const val TEST_FIXTURES = "src/test/resources"
    }
}
