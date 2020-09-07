package nolambda.github.usersearch

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.specs.AbstractStringSpec
import io.kotlintest.specs.StringSpec
import io.mockk.clearAllMocks
import stream.nolambda.karma.Karma

open class PresenterSpec(body: AbstractStringSpec.() -> Unit) : StringSpec({
    Karma.setTestMode()
    body.invoke(this)
}) {
    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }
}