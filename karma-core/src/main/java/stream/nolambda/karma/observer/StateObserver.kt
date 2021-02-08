package stream.nolambda.karma.observer

import androidx.lifecycle.LifecycleOwner

interface StateObserver<T> {
    fun postValue(value: T)
    fun observe(owner: LifecycleOwner, onStateChange: (T) -> Unit)
}