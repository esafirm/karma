package stream.nolambda.karma.differ

/**
 * This is the diffing approach of https://github.com/arkivanov/MVIKotlin
 */

interface ViewRenderer<State> {
    fun render(state: State)
}

inline fun <State : Any> renderer(block: DiffBuilder<State>.() -> Unit): ViewRenderer<State> {
    val builder =
        object : DiffBuilder<State>(), ViewRenderer<State> {
            override fun render(state: State) {
                binders.forEach { it.render(state) }
            }
        }

    builder.block()

    return builder
}

open class DiffBuilder<State : Any> {

    @PublishedApi
    internal val binders = mutableListOf<ViewRenderer<State>>()

    /**
     * Registers the diff strategy
     *
     * @param get a `getter` to extract a piece of data (typically a field value) from the original `Model`
     * @param compare a `comparator` to compare a new value with the old one, default is `equals`
     * @param set a `consumer` of the values, receives the new value if it is the first value or if the `comparator` returned `false`
     */
    inline fun <T> diff(
        crossinline get: (State) -> T,
        crossinline compare: (new: T, old: T) -> Boolean = { a, b -> a == b },
        crossinline set: (T) -> Unit
    ) {
        binders += object : ViewRenderer<State> {
            private var oldValue: T? = null

            override fun render(state: State) {
                val newValue = get(state)
                val oldValue = oldValue
                this.oldValue = newValue

                if ((oldValue == null) || !compare(newValue, oldValue)) {
                    set(newValue)
                }
            }
        }
    }

    inline fun always(crossinline render: (State) -> Unit) {
        binders += object : ViewRenderer<State> {
            override fun render(state: State) {
                render.invoke(state)
            }
        }
    }

    inline fun <T> event(
        crossinline get: (State) -> SingleEvent<T>?,
        crossinline set: (T) -> Unit
    ) {
        binders += object : ViewRenderer<State> {
            override fun render(state: State) {
                get(state).fetch {
                    set(this)
                }
            }
        }
    }
}
