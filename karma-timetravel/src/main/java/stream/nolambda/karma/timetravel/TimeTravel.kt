package stream.nolambda.karma.timetravel

import androidx.lifecycle.LifecycleOwner
import stream.nolambda.karma.KarmaAction
import stream.nolambda.karma.KarmaContextBuilder
import stream.nolambda.karma.utils.KarmaLogger

class TimeTravel<STATE>(
    private val action: KarmaAction<STATE>,
    override val name: String = "TimeTravel"
) : TimeTravelAction, KarmaAction<STATE> {

    private val timeline = mutableListOf<STATE>()
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
     * Select the current state and update the index
     * @param state the selected state
     */
    override fun select(state: Any) {
        val selectedState = cast(state) ?: return
        val index = timeline.indexOf(selectedState)

        if (index >= 0) {
            setStateAndUpdate(selectedState)
        } else {
            KarmaLogger.log { "Timeline doesn't have $state" }
        }
    }

    /**
     * Replace the [oldState] with [newState] and select it
     * @param oldState the old state that contained in [timeline]
     * @param newState the new state that replacing the old state
     */
    override fun replace(oldState: Any, newState: Any) {
        val selectedState = cast(oldState) ?: return
        val castedNewState = cast(newState) ?: return
        val index = timeline.indexOf(selectedState)

        if (index >= 0) {
            timeline.removeAt(index)
            timeline.add(index, castedNewState)
            setStateAndUpdate(castedNewState)
        } else {
            KarmaLogger.log { "Timeline doesn't have $oldState" }
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

    /**
     * Set [currentState] as current time and setState
     * Used for selecting or replacing state in [timeline]
     */
    private fun setStateAndUpdate(currentState: STATE) {
        currentTime = currentState
        setState(currentState)
    }

    private fun setState(currentState: STATE) {
        KarmaLogger.log { "CurrentState $currentState" }

        action.execute {
            set { currentState }
        }
    }

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        action.attach(owner) {
            if (timeline.isEmpty() || !timeline.contains(it)) {
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