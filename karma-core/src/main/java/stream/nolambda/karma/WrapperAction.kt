package stream.nolambda.karma

import androidx.lifecycle.LifecycleOwner

abstract class WrapperAction<STATE>(
    private val originalAction: KarmaAction<STATE>
) : KarmaAction<STATE> {

    override val currentState: STATE
        get() = originalAction.currentState

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        originalAction.attach(owner) {
            onStateUpdate(it)
            onStateChange(it)
        }
    }

    override fun execute(block: suspend KarmaContext<STATE>.() -> Unit) {
        originalAction.execute(block)
    }

    /**
     * Set the state to the Action
     */
    protected fun setState(state: STATE) {
        originalAction.execute {
            setState { state }
        }
    }

    /**
     * onStateUpdate will be triggered when the original action receive onStateChange
     * @param state that changed
     */
    abstract fun onStateUpdate(state: STATE)
}