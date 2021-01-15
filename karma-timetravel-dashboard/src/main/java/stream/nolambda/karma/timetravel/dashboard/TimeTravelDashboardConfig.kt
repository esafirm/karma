package stream.nolambda.karma.timetravel.dashboard

import stream.nolambda.karma.timetravel.dashboard.serializer.GsonStateSerializer
import stream.nolambda.karma.timetravel.dashboard.serializer.StateSerializer

object TimeTravelDashboardConfig {
    var serializer: StateSerializer = GsonStateSerializer()
}