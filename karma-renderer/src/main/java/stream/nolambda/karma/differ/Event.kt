package stream.nolambda.karma.differ

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Event<out T>(private val value: @RawValue T) : Parcelable {
    private var fetched: Boolean = false

    fun get(setFlag: Boolean = true): T? {
        if (fetched) return null
        fetched = setFlag
        return value
    }
}

inline fun <T> Event<T>?.fetch(block: T.() -> Unit) {
    val value = this?.get()
    if (value != null) {
        block.invoke(value)
    }
}

inline fun <T> Event<T>?.fetchCondition(block: T.() -> Boolean) {
    val value = this?.get(false)
    if (value != null) {
        val result = block.invoke(value)
        if (result) {
            // Mark fetch
            this?.get()
        }
    }
}

fun <T> T.asEvent() = Event(this)