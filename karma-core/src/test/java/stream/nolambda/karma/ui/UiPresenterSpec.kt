package stream.nolambda.karma.ui

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import stream.nolambda.karma.Karma
import stream.nolambda.karma.bindTest
import stream.nolambda.karma.ui.fake.PartialUpdatePresenter
import stream.nolambda.karma.ui.fake.TestPresenter

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

    "it should update the nested value" {
        val (p, g) = Karma.bindTest { PartialUpdatePresenter() }

        val expectedValue = "ABC123"
        p.setValue(expectedValue)

        g()?.nested?.value shouldBe expectedValue
    }
})