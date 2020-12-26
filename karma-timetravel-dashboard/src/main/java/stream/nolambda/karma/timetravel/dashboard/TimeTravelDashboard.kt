package stream.nolambda.karma.timetravel.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import stream.nolambda.karma.Karma
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.timetravel.dashboard.databinding.ActivityTimetravelDashboardBinding
import stream.nolambda.karma.ui.ActivityScreen
import stream.nolambda.karma.ui.ScreenViewProvider

class TimeTravelDashboard : ActivityScreen() {

    private lateinit var binding: ActivityTimetravelDashboardBinding

    override fun createView(): ScreenViewProvider = { inflater, _ ->
        binding = ActivityTimetravelDashboardBinding.inflate(inflater)
        binding.root
    }

    override fun onViewCreated(view: View) {
        val renderer = renderer<TimeTravelDashboardState, TimeTravelPresenter> {
            init {
                binding.root.setBackgroundColor(Color.BLACK)
            }
        }

        Karma.bind(
            owner = this,
            presenterCreator = { TimeTravelPresenter() },
            render = renderer::render
        )
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TimeTravelDashboard::class.java).apply {
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }
    }

}