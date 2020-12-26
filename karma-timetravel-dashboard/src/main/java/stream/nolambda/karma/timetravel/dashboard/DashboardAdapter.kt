package stream.nolambda.karma.timetravel.dashboard

import android.content.Context
import nolambda.kommonadapter.simple.SimpleAdapter
import stream.nolambda.karma.timetravel.dashboard.databinding.ItemScreenStateBinding

class DashboardAdapter(context: Context) : SimpleAdapter(context) {
    init {
        create {
            map<Any>(R.layout.item_screen_state) { vh, item ->
                val binding = ItemScreenStateBinding.bind(vh.itemView)
                binding.text.text = item.toString()
            }
        }
    }
}