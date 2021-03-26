package nolambda.github.usersearch.customsavestate

import stream.nolambda.karma.ui.UiPresenter

data class ComplexState(
    val firstState: String = "",
    val secondState: List<String> = emptyList(),
    val ignoredState: Int = 1,
    val nested: NestedComplexState = NestedComplexState()
)

data class NestedComplexState(
    val nestedFirst: String = "",
    val ignoredNested: String = ""
)

class CustomSaveStatePresenter : UiPresenter<ComplexState>(CustomSaveStateHandler()) {
    override fun initialState() = ComplexState()

    fun emit() = setState { ComplexState(nested = NestedComplexState(nestedFirst = "ABC")) }
}