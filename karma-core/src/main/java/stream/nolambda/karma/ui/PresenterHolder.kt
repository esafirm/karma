package stream.nolambda.karma.ui

import androidx.lifecycle.ViewModel
import stream.nolambda.karma.KarmaPresenter

internal class PresenterHolder : ViewModel() {
    var presenter: KarmaPresenter<*>? = null
}