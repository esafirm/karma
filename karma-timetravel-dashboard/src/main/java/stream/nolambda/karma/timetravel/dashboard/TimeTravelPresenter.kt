package stream.nolambda.karma.timetravel.dashboard

import stream.nolambda.karma.Action
import stream.nolambda.karma.differ.asSingleEvent
import stream.nolambda.karma.timetravel.TimeTravelAction
import stream.nolambda.karma.timetravel.TimeTravelEventManager
import stream.nolambda.karma.ui.UiPresenter

class TimeTravelPresenter :
    UiPresenter<TimeTravelDashboardState>(Action { TimeTravelDashboardState() }) {

    private val serializer = TimeTravelDashboardConfig.serializer

    init {
        val timeTravels = TimeTravelEventManager.getAllTimeTravel()
        setState {
            copy(
                timeTravels = timeTravels,
                currentTimeTravel = timeTravels.firstOrNull()
            )
        }
    }

    fun setCurrentTimeTravel(timeTravel: TimeTravelAction) = setState {
        copy(currentTimeTravel = timeTravel)
    }

    fun createEditInfo(state: Any) = setState {
        copy(
            editStateInfo = EditStateInfo(
                clazz = state::class.java,
                state = serializer.toString(state)
            )
        )
    }

    fun clearEditState() = setState {
        copy(editStateInfo = null)
    }

    fun selectEditState(newState: String) = setState {
        val clazz = editStateInfo?.clazz ?: error("No edit state info")
        val newStateObj = serializer.fromString(newState, clazz)
        val oldStateObj = serializer.fromString(editStateInfo.state, clazz)
        copy(
            editStateInfo = null,
            replaceStateEvent = ReplaceStateEvent(
                oldState = oldStateObj,
                newState = newStateObj
            ).asSingleEvent()
        )
    }

}