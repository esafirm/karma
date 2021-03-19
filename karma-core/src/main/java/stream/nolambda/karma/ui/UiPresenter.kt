package stream.nolambda.karma.ui

import androidx.lifecycle.LifecycleOwner
import stream.nolambda.karma.*

typealias ValueCreator<T> = (T) -> T

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

    protected open fun onError(e: Exception) {
        throw e
    }

    protected fun execute(block: suspend KarmaContext<STATE>.() -> Unit) {
        action.execute {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    protected fun setState(change: StateChange<STATE>) {
        action.execute {
            try {
                setState(change)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    protected fun getState() = action.currentState

    fun <T> usePartial(copy: STATE.(ValueCreator<T>) -> STATE): (ValueCreator<T>) -> Unit {
        return { newValueCreator ->
            setState {
                copy(this, newValueCreator)
            }
        }
    }
}
