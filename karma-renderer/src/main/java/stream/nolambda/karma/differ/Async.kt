package stream.nolambda.karma.differ

class Async<T>(
    private val value: Any = Uninitialized
) {
    val isLoading: Boolean get() = value is Loading
    val isError: Boolean get() = value is Failure
    val isUninitialized: Boolean get() = value is Uninitialized

    val isSuccess: Boolean get() = !isUninitialized && !isLoading && !isError

    @Suppress("UNCHECKED_CAST")
    fun get(): T = value as T

    fun getOrNull(): T? = when {
        isSuccess -> get()
        else -> null
    }

    fun getErrorOrNull() = when (value) {
        is Failure -> value.err
        else -> null
    }

    inline fun fetch(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onLoading: () -> Unit = {},
        onUninitialized: () -> Unit = {}
    ) {
        when {
            isLoading -> onLoading()
            isError -> onError(getErrorOrNull() ?: NullPointerException("No error in Async"))
            isSuccess -> onSuccess(get())
            isUninitialized -> onUninitialized()
        }
    }

    internal object Loading
    internal object Uninitialized
    internal class Failure(val err: Throwable)

    override fun equals(other: Any?): Boolean {
        if (other is Async<*>) {
            return value == other.value
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {
        fun <T : Any> success(value: T): Async<T> = Async(value)
        fun <T> failure(exception: Throwable): Async<T> = Async(Failure(exception))
        fun <T> loading(): Async<T> = Async(Loading)
    }
}