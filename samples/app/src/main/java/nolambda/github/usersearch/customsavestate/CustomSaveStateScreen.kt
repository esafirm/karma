package nolambda.github.usersearch.customsavestate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import nolambda.github.usersearch.databinding.ScreenCustomSaveStateBinding
import stream.nolambda.karma.bind
import stream.nolambda.karma.ui.screens.ActivityScreen
import stream.nolambda.karma.ui.screens.viewBinding

class CustomSaveStateScreen : ActivityScreen() {

    override val viewProvider = viewBinding(ScreenCustomSaveStateBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val presenter = CustomSaveStatePresenter()
        val viewBinding = viewProvider.getBinding()

        viewBinding.btnTestBlocking.setOnClickListener {
            presenter.emit()
        }

        bind(
            presenter = { presenter },
            render = { state, _ ->
                viewBinding.txtLogger.text = state.nested.nestedFirst
            }
        )
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CustomSaveStateScreen::class.java))
        }
    }
}