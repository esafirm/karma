package stream.nolambda.karma.timetravel.dashboard

data class TimeTravelDashboardState(
    val timeline: List<Any> = listOf(),
    val currentState: Any? = null
)