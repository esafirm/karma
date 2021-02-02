package stream.nolambda.karma

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import stream.nolambda.karma.config.ActionExecutor
import stream.nolambda.karma.config.ActionExecutorFactory
import stream.nolambda.karma.config.DefaultActionExecutor
import stream.nolambda.karma.config.DefaultActionExecutorFactory
import stream.nolambda.karma.ui.PresenterHolder

object Karma {
    var isTestMode: Boolean = false
    var executor: ActionExecutorFactory = DefaultActionExecutorFactory()
    var enableLog = false

    fun setTestMode() {
        isTestMode = true
    }

    /**
     * Bind all required component to create Karma
     * The [LifecycleOwner] and [ViewModelStoreOwner] is separated to accommodate [Fragment]
     * leaky nature
     *
     * Use the extension function for [Activity]
     */
    fun <S, P : KarmaPresenter<S>> bind(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        presenterCreator: () -> P,
        render: (S, P) -> Unit
    ) {

        val presenterHolder: PresenterHolder by lazy {
            ViewModelProvider(viewModelStoreOwner).get(PresenterHolder::class.java)
        }

        val currentPresenter = presenterHolder.bindPresenter(presenterCreator)

        currentPresenter.attach(lifecycleOwner) {
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

/**
 * extension function for [Karma.bind]
 */
fun <S, O, P : KarmaPresenter<S>> O.bind(
    presenter: () -> P,
    render: (S, P) -> Unit
) where O : LifecycleOwner, O : ViewModelStoreOwner {
    Karma.bind(this, this, presenter, render)
}