package stream.nolambda.karma

import androidx.lifecycle.LifecycleOwner

typealias StateChange<T> = () -> T
typealias StateChangeBuilder<T> = T.() -> T
typealias NameResolver = () -> String

data class KarmaContext<T>(
    val actionName: NameResolver,
    val stateChange: StateChange<T>
)

class KarmaContextBuilder<T>(private val currentState: T) {

    var name: NameResolver = { "" }
    private var stateChange: StateChange<T>? = null

    fun setState(change: StateChangeBuilder<T>) {
        this.stateChange = { change.invoke(currentState) }
    }

    fun build(): KarmaContext<T> =
        KarmaContext(name, stateChange ?: { currentState })
}

interface KarmaPresenter<STATE> {
    fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit)
}

interface KarmaAction<STATE> : KarmaPresenter<STATE> {
    val currentState: STATE
    fun action(block: KarmaContextBuilder<STATE>.() -> Unit)

    operator fun invoke(block: KarmaContextBuilder<STATE>.() -> Unit) {
        action(block)
    }
}