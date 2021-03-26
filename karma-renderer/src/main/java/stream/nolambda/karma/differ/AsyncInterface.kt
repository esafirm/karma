package stream.nolambda.karma.differ

import java.io.Serializable

interface AsyncInterface<T> {
    val isLoading: Boolean
    val isError: Boolean
    val isUninitialized: Boolean
    val isSuccess: Boolean

    fun get(): T
    fun getOrNull(): T?
    fun getErrorOrNull(): Throwable?
}

internal class AsyncImplementation<T>(
    private val value: Any = Uninitialized
) : AsyncInterface<T> {

    override val isLoading: Boolean get() = value is Loading
    override val isError: Boolean get() = value is Failure
    override val isUninitialized: Boolean get() = value is Uninitialized

    override val isSuccess: Boolean get() = !isUninitialized && !isLoading && !isError

    @Suppress("UNCHECKED_CAST")
    override fun get(): T = value as T

    override fun getOrNull(): T? = when {
        isSuccess -> get()
        else -> null
    }

    override fun getErrorOrNull() = when (value) {
        is Failure -> value.err
        else -> null
    }

    internal object Loading : Serializable
    internal object Uninitialized : Serializable
    internal class Failure(val err: Throwable) : Serializable
}

inline fun <T> AsyncInterface<T>.fetch(
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