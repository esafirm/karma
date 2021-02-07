package stream.nolambda.karma.ui

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import stream.nolambda.karma.Karma
import stream.nolambda.karma.bindTest

class TestPresenter : UiPresenter<String>() {
    override fun initialState(): String = "Initial"
    fun setup() = setState { "1" }
    fun testing() = setState { "ABCD" }

    fun testExecute() = execute {
        setState { "1" }
        setState { "2" }
    }
}

class UiPresenterSpec : StringSpec({

    val (presenter, getState) = Karma.bindTest { TestPresenter() }

    "it should have the right state" {
        presenter.setup()
        presenter.testing()

        getState() shouldBe "ABCD"
    }

    "it should have the right state too" {
        presenter.testExecute()

        getState() shouldBe "2"
    }
})