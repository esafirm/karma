package stream.nolambda.karma.timetravel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

object TimeTravelEventManager : TimeTravelEventDispatcher {

    private val listeners = linkedSetOf<TimeTravelAction>()

    @Synchronized
    override fun dispatchEvent(event: TimeTravelEvent) {
        listeners.forEach { listener ->
            when (event) {
                TimeTravelEvent.Forward -> listener.forward()
                TimeTravelEvent.Backward -> listener.back()
                is TimeTravelEvent.Set -> listener.set(event.selectedState)
            }
        }
    }

    @Synchronized
    override fun attach(owner: LifecycleOwner, action: TimeTravelAction) {
        if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            listeners.add(action)
        }
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> listeners.add(action)
                    Lifecycle.Event.ON_DESTROY -> listeners.remove(action)
                    else -> {
                    }
                }
            }
        })
    }

    fun getAllTimeTravel() = listeners.toList()
}

sealed class TimeTravelEvent {
    object Forward : TimeTravelEvent()
    object Backward : TimeTravelEvent()
    data class Set(val selectedState: Any) : TimeTravelEvent()
}