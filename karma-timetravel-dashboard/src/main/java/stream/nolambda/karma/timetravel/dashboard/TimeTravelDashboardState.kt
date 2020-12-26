package stream.nolambda.karma.timetravel.dashboard

data class TimeTravelDashboardState(
    val screeStates: List<String> = listOf(),
    val currentState: String = ""
)