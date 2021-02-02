package stream.nolambda.karma

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class Action<STATE>(private val initialState: () -> STATE) : KarmaAction<STATE> {

    private val stateLiveData: MutableLiveData<STATE> = MutableLiveData()
    private val executor = Karma.executor

    override val currentState get() = stateHolder ?: initialState()

    private var stateHolder: STATE? = null

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            onStateChange.invoke(currentState)
        }
        stateLiveData.observe(owner, Observer {
            onStateChange.invoke(it)
        })
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

    override fun execute(block: KarmaContextBuilder<STATE>.() -> Unit) {
        executor.execute {
            val context = createNextContext(block) // Execute context block
            val (isTheSame, state) = createNewState(context.stateChange) // Execute state change

            if (Karma.isTestMode.not() && isTheSame.not()) {
                stateLiveData.postValue(state)
            }
        }
    }
}