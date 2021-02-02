package stream.nolambda.karma.timetravel.dashboard

import android.app.Activity
import android.content.Intent
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
import stream.nolambda.karma.timetravel.dashboard.editor.StateEditorActivity
import stream.nolambda.karma.timetravel.dashboard.utils.attach
import stream.nolambda.karma.timetravel.dashboard.utils.setOnItemSelected

class TimeTravelDashboard : BottomSheetDialogFragment() {

    private val adapter by lazy { DashboardAdapter(requireContext()) }
    private val presenter by lazy { TimeTravelPresenter() }

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
            presenter = this::presenter,
            render = renderer::render
        )
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == StateEditorActivity.RQ_CODE) {
            val result = data ?: error("No result in $requestCode")
            val newState = result.getStringExtra(StateEditorActivity.RESULT_EXTRA_NEW_STATE)
            presenter.selectEditState(newState!!)
        } else {
            presenter.clearEditState()
        }
    }

    private fun createRenderer(binding: FragmentTimetravelDashboardBinding) =
        renderer<TimeTravelDashboardState, TimeTravelPresenter> { getState ->
            init {
                val onSelectState = { _: Int, state: Any ->
                    TimeTravelEventManager.dispatchEvent(TimeTravelEvent.Select(state))
                }
                val onEditState = { _: Int, state: Any ->
                    createEditInfo(state)
                    true
                }
                binding.recycler.attach(
                    adapter = adapter,
                    onItemClick = onSelectState,
                    onLongClickListener = onEditState
                )

                binding.spinner.setOnItemSelected { pos ->
                    val currentTimeTravel = it.timeTravels.getOrNull(pos)
                    if (currentTimeTravel != null) {
                        setCurrentTimeTravel(currentTimeTravel)
                    }
                }
            }
            event(TimeTravelDashboardState::replaceStateEvent) { event ->
                TimeTravelEventManager.dispatchEvent(
                    TimeTravelEvent.Replace(
                        oldState = event.oldState,
                        newState = event.newState
                    )
                )
            }

            diff(TimeTravelDashboardState::timeTravels) { timeTravels ->
                binding.spinner.attach(timeTravels.map { it.name })
            }
            diff(TimeTravelDashboardState::editStateInfo) { stateInfo ->
                if (stateInfo == null) return@diff
                StateEditorActivity.start(
                    fragment = this@TimeTravelDashboard,
                    stateInfo = stateInfo
                )
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