package nolambda.github.usersearch.playground

import android.content.Context
import android.content.Intent
import nolambda.github.usersearch.databinding.ScreenPlaygroundBinding
import stream.nolambda.karma.bind
import stream.nolambda.karma.differ.fetch
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.ui.screens.ActivityScreen
import stream.nolambda.karma.ui.screens.viewBinding

class PlaygroundScreen : ActivityScreen() {
    override val viewProvider = viewBinding(ScreenPlaygroundBinding::inflate)

    override fun onViewCreated() {
        super.onViewCreated()
        val binding = viewProvider.getBinding()
        binding.container.addView(Logger.getLogView(this))

        val presenter = PlaygroundPresenter()
        val renderer = createRenderer(binding)
        bind(
            presenter = { presenter },
            render = { state, action ->
                Logger.log(state)
                renderer.render(state, action)
            }
        )

        binding.btnTestBlocking.setOnClickListener {
            presenter.testBlocking()
        }

        binding.btnTestErrorHandling.setOnClickListener {
            presenter.testError()
        }

        binding.btnTestPartialState.setOnClickListener {
            presenter.testPartialState()
        }

        binding.btnTestAsync.setOnClickListener {
            presenter.testAsync()
        }
    }

    private fun createRenderer(binding: ScreenPlaygroundBinding) =
        renderer<PlaygroundState, PlaygroundPresenter> {

            always {
                binding.txtLogger.text = it.log
            }

            diff(PlaygroundState::asyncValue) { async ->
                async.fetch(
                    onSuccess = { value -> binding.txtLogger.text = value },
                    onError = { err -> binding.txtLogger.text = err.message },
                    onLoading = { binding.txtLogger.text = "Loading" }
                )
            }
        }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PlaygroundScreen::class.java))
        }
    }
}