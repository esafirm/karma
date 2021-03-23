package nolambda.github.usersearch.customsavestate

import androidx.lifecycle.SavedStateHandle
import stream.nolambda.karma.savedstate.AbsSavedStateHandler

class CustomSaveStateHandler : AbsSavedStateHandler<ComplexState>() {

    override fun mapToState(savedStateHandle: SavedStateHandle): ComplexState? {
        val nested: String = savedStateHandle["nested"] ?: ""
        return ComplexState(nested = NestedComplexState(nestedFirst = nested))
    }

    override fun mapToStateHandle(savedStateHandle: SavedStateHandle, state: ComplexState) {
        savedStateHandle["nested"] = state.nested.nestedFirst
    }
}