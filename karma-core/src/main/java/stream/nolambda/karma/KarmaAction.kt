package stream.nolambda.karma

import androidx.lifecycle.LifecycleOwner

typealias StateChange<T> = T.() -> T

class KarmaContext<T>(
    private val executeFn: (name: String?, StateChange<T>) -> Unit
) {
    fun setState(stateChange: StateChange<T>) {
        executeFn(null, stateChange)
    }
}

interface KarmaPresenter<STATE> {
    fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit)
}

interface KarmaAction<STATE> : KarmaPresenter<STATE> {
    val currentState: STATE
    fun execute(block: suspend KarmaContext<STATE>.() -> Unit)
}