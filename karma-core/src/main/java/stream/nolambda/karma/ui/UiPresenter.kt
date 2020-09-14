package stream.nolambda.karma.ui

import androidx.lifecycle.LifecycleOwner
import stream.nolambda.karma.KarmaAction
import stream.nolambda.karma.KarmaContextBuilder
import stream.nolambda.karma.KarmaPresenter
import stream.nolambda.karma.StateChangeBuilder

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

    /* --------------------------------------------------- */
    /* > Helper / Sugar */
    /* --------------------------------------------------- */

    protected fun execute(block: KarmaContextBuilder<STATE>.() -> Unit) {
        action.execute(block)
    }

    protected fun setState(change: StateChangeBuilder<STATE>) = execute {
        set(change)
    }
}