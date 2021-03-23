package stream.nolambda.karma.savedstate

import androidx.lifecycle.SavedStateHandle

interface SavedStateHandler<STATE> {
    fun init(savedStateHandle: SavedStateHandle)
    fun saveToSavedState(state: STATE)
    fun getSavedState(): STATE?
}