package nolambda.github.usersearch

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks
import stream.nolambda.karma.Karma

abstract class PresenterSpec(body: StringSpec.() -> Unit) : StringSpec({
    Karma.setTestMode()
    body.invoke(this)
}) {
    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }
}