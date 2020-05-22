package io.labs.dotanuki.library.tests

import io.labs.dotanuki.library.Library
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LibraryTests {

    @Test fun `simple test`() {
        assertThat(Library().value).isEqualTo(42)
    }
}