package stream.nolambda.karma.timetravel.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nolambda.kommonadapter.attach
import stream.nolambda.karma.Karma
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.timetravel.TimeTravelEvent
import stream.nolambda.karma.timetravel.TimeTravelEventManager
import stream.nolambda.karma.timetravel.dashboard.databinding.FragmentTimetravelDashboardBinding
import stream.nolambda.karma.timetravel.dashboard.utils.attach

class TimeTravelDashboard : BottomSheetDialogFragment() {

    private val adapter by lazy { DashboardAdapter(requireContext()) }

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
                binding.recycler.attach(adapter = adapter) { _, state ->
                    TimeTravelEventManager.dispatchEvent(TimeTravelEvent.Set(state))
                }
                binding.spinner.setOnItemClickListener { _, _, i, _ ->
                    setCurrentTimeTravel(it.timeTravels[i])
                }
            }
            diff(TimeTravelDashboardState::timeTravels) { timeTravels ->
                binding.spinner.attach(timeTravels.map { it.name })
            }
            always {
                val states = it.currentTimeTravel?.getTimeline() ?: listOf()
                adapter.pushData(states)
            }
        }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            val dashboard = TimeTravelDashboard()
            dashboard.show(fragmentManager, null)
        }
    }

}