package stream.nolambda.karma.timetravel

import androidx.lifecycle.LifecycleOwner
import stream.nolambda.karma.KarmaAction
import stream.nolambda.karma.KarmaContextBuilder
import stream.nolambda.karma.utils.KarmaLogger

class TimeTravel<STATE>(
    private val _action: KarmaAction<STATE>
) : TimeTravelAction, KarmaAction<STATE> {

    private val timeline = linkedSetOf<STATE>()
    private var currentTime: STATE? = null

    /**
     * Forward the time
     * Addition to back stack also happen on [attach]
     */
    override fun forward() {
        if (currentTime == timeline.last()) {
            KarmaLogger.log { "Cannot move forward" }
            return
        }

        val index = timeline.indexOf(currentTime)
        setState(timeline.elementAt(index + 1))
    }

    /**
     * Back the timex
     * Addition to forward stack happen here
     */
    override fun back() {
        if (currentTime == timeline.first()) {
            KarmaLogger.log { "Cannot move back" }
            return
        }

        val index = timeline.indexOf(currentTime)
        setState(timeline.elementAt(index - 1))
    }

    private fun setState(currentState: STATE) {
        KarmaLogger.log { "CurrentState $currentState" }

        _action {
            setState { currentState }
        }
    }

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        _action.attach(owner) {
            if (timeline.isEmpty() || currentTime == timeline.last()) {
                timeline.add(it)
            }
            currentTime = it
            onStateChange.invoke(it)
        }
    }

    override val currentState: STATE
        get() = _action.currentState

    override fun action(block: KarmaContextBuilder<STATE>.() -> Unit) {
        _action.action(block)
    }
}