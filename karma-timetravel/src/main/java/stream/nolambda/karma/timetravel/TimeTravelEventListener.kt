package stream.nolambda.karma.timetravel

import androidx.lifecycle.LifecycleOwner

interface TimeTravelEventListener {
    fun bindTimeTravel(owner: LifecycleOwner, action: TimeTravelAction) {
        TimeTravelEventManager.attach(owner, action)
    }
}