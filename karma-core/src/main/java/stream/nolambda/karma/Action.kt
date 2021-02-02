package stream.nolambda.karma

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class Action<STATE>(private val initialState: () -> STATE) : KarmaAction<STATE> {

    private val stateLiveData: MutableLiveData<STATE> = MutableLiveData()
    private val executor = Karma.executor.getExecutor(Karma.isTestMode)

    override val currentState get() = stateHolder ?: initialState()

    private var stateHolder: STATE? = null

    private val context = KarmaContext(this::executeFn)

    override fun attach(owner: LifecycleOwner, onStateChange: (STATE) -> Unit) {
        if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            onStateChange.invoke(currentState)
        }
        stateLiveData.observe(owner, {
            onStateChange.invoke(it)
        })
    }

    private fun createNewState(stateModifier: StateChange<STATE>): Pair<Boolean, STATE> {
        val newState = stateModifier.invoke(currentState)
        val isTheSame = stateHolder == newState
        stateHolder = newState
        return isTheSame to newState
    }

    override fun execute(block: suspend KarmaContext<STATE>.() -> Unit) {
        executor.execute {
            block(context)
        }
    }

    private fun executeFn(name: String?, stateChange: StateChange<STATE>) {
        val (isTheSame, state) = createNewState(stateChange)

        if (Karma.isTestMode.not() && isTheSame.not()) {
            stateLiveData.postValue(state)
        }
    }
}