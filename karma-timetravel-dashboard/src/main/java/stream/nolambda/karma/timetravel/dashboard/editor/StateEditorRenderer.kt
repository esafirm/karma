package stream.nolambda.karma.timetravel.dashboard.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakaztahir.codeeditor.model.CodeLang
import com.wakaztahir.codeeditor.prettify.PrettifyParser
import com.wakaztahir.codeeditor.theme.CodeThemeType
import com.wakaztahir.codeeditor.utils.parseCodeAsAnnotatedString
import stream.nolambda.karma.differ.ViewRenderer
import stream.nolambda.karma.timetravel.dashboard.R
import stream.nolambda.karma.ui.StaticPresenter

internal data class EditorState(
    val stateString: String,
)

internal interface StateEditorListener {
    fun onSave(state: EditorState)
}

internal fun stateEditorRenderer(
    composeView: ComposeView,
    listener: StateEditorListener,
) = object : ViewRenderer<EditorState, StaticPresenter<EditorState>> {
    override fun render(state: EditorState, action: StaticPresenter<EditorState>) {
        composeView.setContent {
            StateEditor(state = state, onSave = { lastState -> listener.onSave(state.copy(stateString = lastState)) })
        }
    }
}

private val MONOKAI = Color(0XFF272822)

@Composable
private fun StateEditor(
    state: EditorState,
    onSave: (finalState: String) -> Unit,
) {
    val language = CodeLang.JSON
    val parser = remember { PrettifyParser() }
    val theme = remember { CodeThemeType.Monokai.theme }

    val parsedCode = remember {
        parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = state.stateString
        )
    }

    val (textFieldValue, setTextViewValue) = remember {
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MONOKAI)
    ) {
        BasicTextField(
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            value = textFieldValue,
            onValueChange = { changed ->
                setTextViewValue(
                    changed.copy(
                        annotatedString = parseCodeAsAnnotatedString(
                            parser = parser,
                            theme = theme,
                            lang = language,
                            code = changed.text
                        )
                    )
                )
            },
        )

        val onClick = remember(textFieldValue) {
            { onSave(textFieldValue.text) }
        }

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.save_state))
        }
    }
}
