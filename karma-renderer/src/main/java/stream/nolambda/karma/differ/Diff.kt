package stream.nolambda.karma.differ

/**
 * This is the diffing approach of https://github.com/arkivanov/MVIKotlin
 */

interface StateGetter<S> {
    fun getState(): S
}

interface ViewRenderer<S, A> {
    fun render(state: S, action: A)
}

class ParentRenderer<S : Any, A> : DiffBuilder<S, A>(), ViewRenderer<S, A>, StateGetter<S> {

    private var internalState: S? = null

    override fun render(state: S, action: A) {
        internalState = state
        binders.forEach { it.render(state, action) }
    }

    override fun getState(): S = internalState!!
}

inline fun <S : Any, A> renderer(
    block: DiffBuilder<S, A>.(getState: () -> S) -> Unit
): ViewRenderer<S, A> {
    val builder = ParentRenderer<S, A>()
    val getState = { builder.getState() }
    builder.block(getState)
    return builder
}

open class DiffBuilder<S : Any, A> {

    @PublishedApi
    internal val binders = mutableListOf<ViewRenderer<S, A>>()

    fun init(render: A.(S) -> Unit) {
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
    fun <T> diff(
        get: (S) -> T,
        compare: (new: T, old: T) -> Boolean = { a, b -> a == b },
        set: A.(T) -> Unit
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

    fun always(render: A.(S) -> Unit) {
        binders += object : ViewRenderer<S, A> {
            override fun render(state: S, action: A) {
                render.invoke(action, state)
            }
        }
    }

    fun <T> event(
        get: (S) -> SingleEvent<T>?,
        set: A.(T) -> Unit
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
