package stream.nolambda.karma.timetravel.dashboard.editor

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import stream.nolambda.karma.bind
import stream.nolambda.karma.timetravel.dashboard.EditStateInfo
import stream.nolambda.karma.ui.StaticPresenter
import java.io.Serializable

class StateEditorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val passedState: EditStateInfo = intent.getSerializable(
            EXTRA_STATE_INFO,
            EditStateInfo::class.java
        ) ?: error("No state info passed")

        val composeView = ComposeView(this)
        setContentView(composeView)

        val renderer = stateEditorRenderer(composeView, object : StateEditorListener {
            override fun onSave(state: EditorState) {
                val dataIntent = Intent().apply {
                    putExtra(RESULT_EXTRA_NEW_STATE, state.stateString)
                }
                setResult(RESULT_OK, dataIntent)
                finish()
            }
        })
        bind(
            presenter = { StaticPresenter(EditorState(passedState.state)) },
            render = renderer::render
        )
    }

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    private fun <T : Serializable> Intent.getSerializable(extra: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializableExtra(extra, clazz)
        } else {
            extras?.getSerializable(extra) as? T
        }
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
