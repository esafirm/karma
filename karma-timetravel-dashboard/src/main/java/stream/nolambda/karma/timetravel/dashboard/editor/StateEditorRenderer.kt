package stream.nolambda.karma.timetravel.dashboard.editor

import de.markusressel.kodehighlighter.core.util.EditTextHighlighter
import de.markusressel.kodehighlighter.language.json.JsonRuleBook
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.timetravel.dashboard.databinding.RendererStateEditorBinding
import stream.nolambda.karma.ui.StaticPresenter

data class EditorState(
    val stateString: String,
)

interface StateEditorListener {
    fun onSave(state: EditorState)
}

fun stateEditorRenderer(
    binding: RendererStateEditorBinding,
    listener: StateEditorListener
) = renderer<EditorState, StaticPresenter<EditorState>> {
    init {

        val editTextHighlighter = EditTextHighlighter(
            target = binding.inpStateEditor,
            languageRuleBook = JsonRuleBook()
        )
        editTextHighlighter.start()

        binding.inpStateEditor.setText(it.stateString)
        binding.btnSave.setOnClickListener {
            listener.onSave(EditorState(binding.inpStateEditor.text.toString()))
        }
    }
}
