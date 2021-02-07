package stream.nolambda.karma

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal val TEST_OWNER = LifecycleOwner {
    object : Lifecycle() {
        override fun addObserver(observer: LifecycleObserver) {
        }

        override fun removeObserver(observer: LifecycleObserver) {
        }

        override fun getCurrentState(): State = State.INITIALIZED
    }
}

/**
 * extension function for testing
 */
fun <S, P : KarmaPresenter<S>> Karma.bindTest(creator: () -> P): Pair<P, () -> S?> {
    var lastState: S? = null
    setTestMode()
    val presenter = creator()
    presenter.attach(TEST_OWNER) {
        lastState = it
    }
    return presenter to { lastState }
}
