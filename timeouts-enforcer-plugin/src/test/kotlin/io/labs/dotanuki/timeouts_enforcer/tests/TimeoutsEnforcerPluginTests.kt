package io.labs.dotanuki.timeouts_enforcer.tests

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class TimeoutsEnforcerPluginTests {

    @get:Rule val tempFolder = TemporaryFolder()

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

    @Test fun `should timeout with slow task`() {
        val fixtureDir = prepareFixture(perTaskSpec = "5.seconds")
        val millisToDelay = 10000

        val build = GradleRunner.create()
            .withProjectDir(fixtureDir)
            .withPluginClasspath()
            .withGradleVersion("6.3")
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
