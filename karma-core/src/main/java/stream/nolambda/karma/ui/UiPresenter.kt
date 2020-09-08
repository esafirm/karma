package stream.nolambda.karma.ui

import androidx.lifecycle.LifecycleOwner
import stream.nolambda.karma.KarmaAction
import stream.nolambda.karma.KarmaPresenter

abstract class UiPresenter<STATE>(
    private val action: KarmaAction<STATE>
) : KarmaPresenter<STATE> {

    open fun onAttach() {
        // no-op
    }

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        action.attach(owner, onStateChange)

        onAttach()
    }
}