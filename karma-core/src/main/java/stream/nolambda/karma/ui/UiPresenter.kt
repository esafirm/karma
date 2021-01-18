package stream.nolambda.karma.ui

import androidx.lifecycle.LifecycleOwner
import stream.nolambda.karma.*

abstract class UiPresenter<STATE> : KarmaPresenter<STATE> {

    private fun defaultInitialState(): Nothing = TODO(
        """
        Please initialize the default state via:
        1. Override initialState() in Presenter
        2. Override action property in Presenter
        """.trimIndent()
    )

    protected open val action: KarmaAction<STATE> = Action(::initialState)

    open fun initialState(): STATE = defaultInitialState()

    open fun onAttach(owner: LifecycleOwner) {
        // no-op
    }

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        action.attach(owner, onStateChange)

        onAttach(owner)
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