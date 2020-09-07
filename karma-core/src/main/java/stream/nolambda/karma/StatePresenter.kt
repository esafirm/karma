package stream.nolambda.karma

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

abstract class StatePresenter<STATE> : KarmaAction<STATE> {

    private val stateLiveData: MutableLiveData<STATE> = MutableLiveData()
    private val executor = Karma.executor

    override val currentState get() = stateHolder ?: initialState()

    private var stateHolder: STATE? = null

    internal open fun onAttach() {
        // no-op
    }

    fun attach(lifecycleOwner: LifecycleOwner, onState: (STATE) -> Unit) {
        stateLiveData.observe(lifecycleOwner, Observer {
            onState.invoke(it)
        })

        onAttach()
    }

    private fun createNewState(stateModifier: () -> STATE): Pair<Boolean, STATE> {
        val newState = stateModifier.invoke()
        val isTheSame = stateHolder == newState
        stateHolder = newState
        return isTheSame to newState
    }

    private fun createNextContext(block: KarmaContextBuilder<STATE>.() -> Unit): KarmaContext<STATE> {
        return KarmaContextBuilder(currentState).apply(block).build()
    }

    override fun action(block: KarmaContextBuilder<STATE>.() -> Unit) {
        executor.execute {
            val context = createNextContext(block)
            val (isTheSame, state) = createNewState(context.stateChange)

            if (Karma.isTestMode.not() && isTheSame.not()) {
                stateLiveData.postValue(state)
            }
        }
    }
}