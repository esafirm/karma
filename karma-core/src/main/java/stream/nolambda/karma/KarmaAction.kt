package stream.nolambda.karma

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

interface KarmaAction<STATE> {
    val currentState: STATE
    fun initialState(): STATE
    fun action(block: KarmaContextBuilder<STATE>.() -> Unit)
}