package stream.nolambda.karma.timetravel.dashboard.editor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.wakaztahir.codeeditor.model.CodeLang
import com.wakaztahir.codeeditor.prettify.PrettifyParser
import com.wakaztahir.codeeditor.theme.CodeThemeType
import com.wakaztahir.codeeditor.utils.parseCodeAsAnnotatedString
import stream.nolambda.karma.differ.ViewRenderer
import stream.nolambda.karma.timetravel.dashboard.R
import stream.nolambda.karma.ui.StaticPresenter

data class EditorState(
    val stateString: String,
)

interface StateEditorListener {
    fun onSave(state: EditorState)
}

fun stateEditorRenderer(
    composeView: ComposeView,
    listener: StateEditorListener,
) = object : ViewRenderer<EditorState, StaticPresenter<EditorState>> {
    override fun render(state: EditorState, action: StaticPresenter<EditorState>) {
        composeView.setContent {
            StateEditor(state = state, onSave = { listener.onSave(state) })
        }
    }
}

@Composable
private fun StateEditor(
    state: EditorState,
    onSave: () -> Unit,
) {
    val language = CodeLang.JSON

    val parser = remember { PrettifyParser() } // try getting from LocalPrettifyParser.current
    val theme = CodeThemeType.Monokai.theme

    val parsedCode = remember {
        parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = state.stateString
        )
    }

    val textFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                annotatedString = parseCodeAsAnnotatedString(
                    parser = parser,
                    theme = theme,
                    lang = language,
                    code = parsedCode.text
                )
            )
        )
    }

    Row {
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = textFieldValue.value,
            onValueChange = { textFieldValue.value = it },
            label = { Text("State") }
        )
        Button(onClick = onSave) {
            Text(text = stringResource(id = R.string.save_state))
        }
    }
}
