package stream.nolambda.karma.timetravel.dashboard

import android.content.Context
import nolambda.kommonadapter.simple.SimpleAdapter
import nolambda.kommonadapter.viewbinding.map
import stream.nolambda.karma.timetravel.dashboard.databinding.ItemScreenStateBinding

class DashboardAdapter(context: Context) : SimpleAdapter(context) {
    init {
        create {
            map(ItemScreenStateBinding::inflate, Any::class) { _, state ->
                txtState.text = state.toString()
            }
        }
    }
}