package stream.nolambda.karma.timetravel

import androidx.lifecycle.LifecycleOwner

interface TimeTravelEventDispatcher {
    fun dispatchEvent(event: TimeTravelEvent)
    fun attach(owner: LifecycleOwner, action: TimeTravelAction)
}