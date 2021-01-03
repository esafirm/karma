package stream.nolambda.karma.timetravel.dashboard

import stream.nolambda.karma.Action
import stream.nolambda.karma.timetravel.TimeTravelAction
import stream.nolambda.karma.timetravel.TimeTravelEventManager
import stream.nolambda.karma.ui.UiPresenter

class TimeTravelPresenter :
    UiPresenter<TimeTravelDashboardState>(Action { TimeTravelDashboardState() }) {

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

}