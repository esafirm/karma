package stream.nolambda.karma.timetravel.dashboard.editor

import stream.nolambda.karma.Action
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.timetravel.dashboard.databinding.RendererStateEditorBinding
import stream.nolambda.karma.ui.UiPresenter

data class EditorState(
    val stateString: String,
)

interface StateEditorListener {
    fun onSave(state: EditorState)
}

fun stateEditorRenderer(
    binding: RendererStateEditorBinding,
    listener: StateEditorListener
) = renderer<EditorState, UnitPresenter> {
    init {
        binding.inpStateEditor.setText(it.stateString)
        binding.btnSave.setOnClickListener {
            listener.onSave(EditorState(binding.inpStateEditor.text.toString()))
        }
    }
}

class UnitPresenter(state: String) : UiPresenter<EditorState>(Action { EditorState(state) }) {

}
