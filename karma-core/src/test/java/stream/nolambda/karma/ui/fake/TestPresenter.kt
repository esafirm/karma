package stream.nolambda.karma.ui.fake

import stream.nolambda.karma.ui.UiPresenter

class TestPresenter : UiPresenter<String>() {
    override fun initialState(): String = "Initial"
    fun setup() = setState { "1" }
    fun testing() = setState { "ABCD" }

    fun testExecute() = execute {
        setState { "1" }
        setState { "2" }
    }
}

data class TestState(
    val nested: NestedTestState = NestedTestState()
)

data class NestedTestState(
    val value: String = ""
)

class PartialUpdatePresenter : UiPresenter<TestState>() {

    private val setNestedValue = usePartial<String> {
        copy(nested = nested.copy(value = it(nested.value)))
    }

    override fun initialState(): TestState = TestState()

    fun setValue(value: String) = setNestedValue { value }
}