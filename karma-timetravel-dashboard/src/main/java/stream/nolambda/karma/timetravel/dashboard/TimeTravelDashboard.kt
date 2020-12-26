package stream.nolambda.karma.timetravel.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import stream.nolambda.karma.Karma
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.timetravel.dashboard.databinding.FragmentTimetravelDashboardBinding

class TimeTravelDashboard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTimetravelDashboardBinding.inflate(inflater, container, false)
        val renderer = createRenderer(binding)
        Karma.bind(
            lifecycleOwner = viewLifecycleOwner,
            viewModelStoreOwner = this,
            presenterCreator = { TimeTravelPresenter() },
            render = renderer::render
        )
        return binding.root
    }

    private fun createRenderer(binding: FragmentTimetravelDashboardBinding) =
        renderer<TimeTravelDashboardState, TimeTravelPresenter> {
            init {

            }
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