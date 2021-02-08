package stream.nolambda.karma.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class LiveDataStateObserver<T> : StateObserver<T> {

    private val stateLiveData: MutableLiveData<T> = MutableLiveData()

    override fun postValue(value: T) {
        stateLiveData.postValue(value)
    }

    override fun observe(owner: LifecycleOwner, onStateChange: (T) -> Unit) {
        stateLiveData.observe(owner) {
            onStateChange(it)
        }
    }
}