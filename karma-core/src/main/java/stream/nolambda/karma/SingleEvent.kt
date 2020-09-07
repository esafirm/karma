package stream.nolambda.karma

class SingleEvent<out T>(private val value: T) {
    private var fetched: Boolean = false

    fun get(setFlag: Boolean = true): T? {
        if (fetched) return null
        fetched = setFlag
        return value
    }
}

inline fun <T> SingleEvent<T>?.fetch(block: T.() -> Unit) {
    val value = this?.get()
    if (value != null) {
        block.invoke(value)
    }
}

inline fun <T> SingleEvent<T>?.fetchCondition(block: T.() -> Boolean) {
    val value = this?.get(false)
    if (value != null) {
        val result = block.invoke(value)
        if (result) {
            // Mark fetch
            this?.get()
        }
    }
}

fun <T> T.asSingleEvent() = SingleEvent(this)