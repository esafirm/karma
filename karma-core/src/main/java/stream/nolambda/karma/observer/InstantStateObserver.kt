package stream.nolambda.karma.observer

import androidx.lifecycle.LifecycleOwner

class InstantStateObserver<T> : StateObserver<T> {

    private var onStateChange: ((T) -> Unit)? = null

    override fun postValue(value: T) {
        onStateChange?.invoke(value)
    }

    override fun observe(owner: LifecycleOwner, onStateChange: (T) -> Unit) {
        this.onStateChange = onStateChange
    }
}