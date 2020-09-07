package stream.nolambda.karma

import androidx.lifecycle.ViewModel

internal class PresenterHolder : ViewModel() {
    var presenter: stream.nolambda.karma.StatePresenter<*>? = null
}