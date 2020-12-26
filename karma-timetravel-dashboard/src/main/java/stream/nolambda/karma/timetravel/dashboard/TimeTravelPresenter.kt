package stream.nolambda.karma.timetravel.dashboard

import stream.nolambda.karma.Action
import stream.nolambda.karma.timetravel.TimeTravelEventManager
import stream.nolambda.karma.ui.UiPresenter

class TimeTravelPresenter :
    UiPresenter<TimeTravelDashboardState>(Action { TimeTravelDashboardState() }) {

    private val manager = TimeTravelEventManager.getAllTimeTravel()

}