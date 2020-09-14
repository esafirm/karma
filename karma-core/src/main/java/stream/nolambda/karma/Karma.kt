package stream.nolambda.karma

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import stream.nolambda.karma.config.ActionExecutor
import stream.nolambda.karma.config.DefaultActionExecutor
import stream.nolambda.karma.ui.PresenterHolder

object Karma {
    var isTestMode: Boolean = false
    var executor: ActionExecutor = DefaultActionExecutor()
    var enableLog = false

    fun setTestMode() {
        isTestMode = true
    }

    fun <S, O, P : KarmaPresenter<S>> bind(
        owner: O,
        presenterCreator: () -> P,
        render: (S, P) -> Unit
    ) where O : LifecycleOwner, O : ViewModelStoreOwner {

        val presenterHolder: PresenterHolder by lazy {
            ViewModelProvider(owner).get(PresenterHolder::class.java)
        }

        val currentPresenter = presenterHolder.bindPresenter(presenterCreator)

        currentPresenter.attach(owner) {
            render(it, currentPresenter)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : KarmaPresenter<*>> PresenterHolder.bindPresenter(block: () -> T): T {
        val currentPresenter = presenter ?: block()
        presenter = currentPresenter
        return currentPresenter as T
    }
}