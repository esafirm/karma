package stream.nolambda.karma.timetravel.dashboard

import stream.nolambda.karma.timetravel.TimeTravelAction

data class TimeTravelDashboardState(
    val timeTravels: List<TimeTravelAction> = listOf(),
    val currentTimeTravel: TimeTravelAction? = null
)