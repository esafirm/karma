package nolambda.github.usersearch.playground

import android.content.Context
import android.content.Intent
import nolambda.github.usersearch.databinding.ScreenPlaygroundBinding
import stream.nolambda.karma.bind
import stream.nolambda.karma.ui.screens.ActivityScreen
import stream.nolambda.karma.ui.screens.viewBinding

class PlaygroundScreen : ActivityScreen() {
    override val viewProvider = viewBinding(ScreenPlaygroundBinding::inflate)

    override fun onViewCreated() {
        super.onViewCreated()
        val binding = viewProvider.getBinding()
        binding.container.addView(Logger.getLogView(this))

        val presenter = PlaygroundPresenter()
        bind(
            presenterCreator = { presenter },
            render = { state, _ ->
                Logger.log(state)
            }
        )

        presenter.actionOne()
        presenter.actionTwo()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PlaygroundScreen::class.java))
        }
    }
}