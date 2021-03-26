package stream.nolambda.karma.savedstate

import androidx.lifecycle.SavedStateHandle

abstract class AbsSavedStateHandler<STATE> : SavedStateHandler<STATE> {

    private lateinit var saveState: SavedStateHandle

    override fun init(savedStateHandle: SavedStateHandle) {
        this.saveState = savedStateHandle
    }

    override fun saveToSavedState(state: STATE) {
        mapToStateHandle(saveState, state)
    }

    override fun getSavedState(): STATE? = mapToState(saveState)

    abstract fun mapToState(savedStateHandle: SavedStateHandle): STATE?
    abstract fun mapToStateHandle(savedStateHandle: SavedStateHandle, state: STATE)
}