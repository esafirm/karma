package stream.nolambda.karma.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import stream.nolambda.karma.KarmaPresenter
import stream.nolambda.karma.savedstate.SavedStateHandler

internal class PresenterHolder(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var presenter: KarmaPresenter<*>? = null
        set(value) {
            field = value
            if (value is SavedStateHandler<*>) {
                value.init(savedStateHandle)
            }
        }
}