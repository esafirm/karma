package stream.nolambda.karma.differ

/**
 * This is the diffing approach of https://github.com/arkivanov/MVIKotlin
 */

interface ViewRenderer<S, A> {
    fun render(state: S, action: A)
}

inline fun <S : Any, A> renderer(
    block: DiffBuilder<S, A>.() -> Unit
): ViewRenderer<S, A> {
    val builder = object : DiffBuilder<S, A>(), ViewRenderer<S, A> {
        override fun render(state: S, action: A) {
            binders.forEach { it.render(state, action) }
        }
    }

    builder.block()

    return builder
}

open class DiffBuilder<S : Any, A> {

    @PublishedApi
    internal val binders = mutableListOf<ViewRenderer<S, A>>()

    inline fun init(crossinline render: A.(S) -> Unit) {
        binders += object : ViewRenderer<S, A> {

            var isAlreadyRender = false

            override fun render(state: S, action: A) {
                if (!isAlreadyRender) {
                    isAlreadyRender = true
                    render.invoke(action, state)
                }
            }
        }
    }

    /**
     * Registers the diff strategy
     *
     * @param get a `getter` to extract a piece of data (typically a field value) from the original `Model`
     * @param compare a `comparator` to compare a new value with the old one, default is `equals`
     * @param set a `consumer` of the values, receives the new value if it is the first value or if the `comparator` returned `false`
     */
    inline fun <T> diff(
        crossinline get: (S) -> T,
        crossinline compare: (new: T, old: T) -> Boolean = { a, b -> a == b },
        crossinline set: A.(T) -> Unit
    ) {
        binders += object : ViewRenderer<S, A> {
            private var oldValue: T? = null

            override fun render(state: S, action: A) {
                val newValue = get(state)
                val oldValue = oldValue
                this.oldValue = newValue

                if ((oldValue == null) || !compare(newValue, oldValue)) {
                    set(action, newValue)
                }
            }
        }
    }

    inline fun always(crossinline render: A.(S) -> Unit) {
        binders += object : ViewRenderer<S, A> {
            override fun render(state: S, action: A) {
                render.invoke(action, state)
            }
        }
    }

    inline fun <T> event(
        crossinline get: (S) -> SingleEvent<T>?,
        crossinline set: A.(T) -> Unit
    ) {
        binders += object : ViewRenderer<S, A> {
            override fun render(state: S, action: A) {
                get(state).fetch {
                    set(action, this)
                }
            }
        }
    }
}
