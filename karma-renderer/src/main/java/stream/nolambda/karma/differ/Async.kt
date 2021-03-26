package stream.nolambda.karma.differ

import java.io.Serializable

class Async<T>(
    private val value: Any = AsyncImplementation.Uninitialized
) : AsyncInterface<T> by AsyncImplementation(value), Serializable {

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
        fun <T> failure(exception: Throwable): Async<T> =
            Async(AsyncImplementation.Failure(exception))

        fun <T> loading(): Async<T> = Async(AsyncImplementation.Loading)
    }
}