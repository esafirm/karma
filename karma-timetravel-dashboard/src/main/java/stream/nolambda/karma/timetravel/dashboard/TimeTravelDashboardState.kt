package stream.nolambda.karma.timetravel.dashboard

import stream.nolambda.karma.differ.SingleEvent
import stream.nolambda.karma.timetravel.TimeTravelAction
import java.io.Serializable

data class EditStateInfo(
    val clazz: Class<*>,
    val state: String
) : Serializable

data class ReplaceStateEvent(
    val oldState: Any,
    val newState: Any
)

data class TimeTravelDashboardState(
    val timeTravels: List<TimeTravelAction> = listOf(),
    val currentTimeTravel: TimeTravelAction? = null,
    val editStateInfo: EditStateInfo? = null,
    val replaceStateEvent: SingleEvent<ReplaceStateEvent>? = null,
)