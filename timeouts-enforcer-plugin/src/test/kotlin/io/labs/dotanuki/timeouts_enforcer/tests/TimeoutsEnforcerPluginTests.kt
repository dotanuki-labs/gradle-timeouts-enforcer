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
        val fixtureDir = prepareFixture("healthy")

        val build = GradleRunner.create()
            .withProjectDir(fixtureDir)
            .withPluginClasspath()
            .withArguments("clean", "run", "--info")
            .build()

        assertThat(build.output).contains("BUILD SUCCESSFUL")
    }

    @Test fun `should break project with unsupported Gradle version`() {
    }

    @Test fun `should timeout with slow task`() {
    }

    @Test fun `should timeout with slow build`() {
    }

    private fun prepareFixture(name: String): File = tempFolder.newFolder().apply {
        File("$TEST_FIXTURES/$name").copyRecursively(this)
    }

    companion object {
        private const val TEST_FIXTURES = "src/test/resources"
    }
}