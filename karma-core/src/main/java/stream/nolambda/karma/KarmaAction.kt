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

    fun set(change: StateChangeBuilder<T>) {
        if (stateChange != null) {
            error("Only one set() will be called in this context. Please change your code")
        }
        stateChange = { change.invoke(currentState) }
    }

    /**
     * Create [KarmaContext] from the builder
     * This should only called by the internal
     */
    internal fun build(): KarmaContext<T> =
        KarmaContext(name, stateChange ?: { currentState })
}

interface KarmaPresenter<STATE> {
    fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit)
}

interface KarmaAction<STATE> : KarmaPresenter<STATE> {
    val currentState: STATE
    fun execute(block: KarmaContextBuilder<STATE>.() -> Unit)
}