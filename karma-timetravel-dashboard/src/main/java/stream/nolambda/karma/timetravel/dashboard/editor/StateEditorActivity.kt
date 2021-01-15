package stream.nolambda.karma.timetravel.dashboard.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import stream.nolambda.karma.bind
import stream.nolambda.karma.timetravel.dashboard.EditStateInfo
import stream.nolambda.karma.timetravel.dashboard.databinding.RendererStateEditorBinding

class StateEditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = RendererStateEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val passedState: EditStateInfo =
            intent.extras?.getSerializable(EXTRA_STATE_INFO) as? EditStateInfo
                ?: error("no current state")

        val renderer = stateEditorRenderer(binding, object : StateEditorListener {
            override fun onSave(state: EditorState) {
                val dataIntent = Intent().apply {
                    putExtra(RESULT_EXTRA_NEW_STATE, state.stateString)
                }
                setResult(RESULT_OK, dataIntent)
                finish()
            }
        })
        bind(
            presenterCreator = { UnitPresenter(passedState.state) },
            render = renderer::render
        )
    }

    companion object {

        private const val EXTRA_STATE_INFO = "StateEditor.State"

        const val RESULT_EXTRA_NEW_STATE = "StateEditor.NewState"
        const val RQ_CODE = 111

        fun start(fragment: Fragment, stateInfo: EditStateInfo) {
            val intent = Intent(fragment.requireContext(), StateEditorActivity::class.java).apply {
                putExtra(EXTRA_STATE_INFO, stateInfo)
            }
            fragment.startActivityForResult(intent, RQ_CODE)
        }

        fun start(context: Activity, stateInfo: EditStateInfo) {
            val intent = Intent(context, StateEditorActivity::class.java).apply {
                putExtra(EXTRA_STATE_INFO, stateInfo)
            }
            context.startActivity(intent)
        }
    }
}