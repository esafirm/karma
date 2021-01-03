package stream.nolambda.karma.timetravel

import androidx.lifecycle.LifecycleOwner
import stream.nolambda.karma.KarmaAction
import stream.nolambda.karma.KarmaContextBuilder
import stream.nolambda.karma.utils.KarmaLogger

class TimeTravel<STATE>(
    private val action: KarmaAction<STATE>,
    override val name: String = "TimeTravel"
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
     * Back the time
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

    /**
     * This is used for time travel dashboard
     * @return collection of the state (timeline)
     */
    @Suppress("UNCHECKED_CAST")
    override fun getTimeline(): List<Any> = timeline.toList() as List<Any>


    /**
     * Set the current state and update the index
     * @param state the selected state
     */
    override fun set(state: Any) {
        val selectedState = cast(state) ?: return
        val index = timeline.indexOf(selectedState)

        if (index >= 0) {
            setState(timeline.elementAt(index))
        } else {
            KarmaLogger.log { "Timeline doesn't have $state" }
        }
    }

    private fun cast(state: Any): STATE? {
        return try {
            @Suppress("UNCHECKED_CAST")
            state as STATE
        } catch (e: Exception) {
            null
        }
    }

    private fun setState(currentState: STATE) {
        KarmaLogger.log { "CurrentState $currentState" }

        action.execute {
            set { currentState }
        }
    }

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        action.attach(owner) {
            if (timeline.isEmpty() || currentTime == timeline.last()) {
                timeline.add(it)
            }
            currentTime = it
            onStateChange.invoke(it)
        }
    }

    override val currentState: STATE
        get() = action.currentState

    override fun execute(block: KarmaContextBuilder<STATE>.() -> Unit) {
        action.execute(block)
    }
}