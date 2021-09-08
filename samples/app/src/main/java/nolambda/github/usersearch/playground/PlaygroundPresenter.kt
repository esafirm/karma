package nolambda.github.usersearch.playground

import kotlinx.coroutines.delay
import stream.nolambda.karma.KarmaContext
import stream.nolambda.karma.differ.Async
import stream.nolambda.karma.ui.UiPresenter

class PlaygroundPresenter : UiPresenter<PlaygroundState>() {
    override fun initialState() = PlaygroundState()

    private val setNestedState = usePartial<NestedState> {
        copy(nestedState = it(nestedState))
    }

    private val setAsync = usePartial<Async<String>> { copy(asyncValue = it(asyncValue)) }

    fun testBlocking() {
        actionOne()
        actionTwo()
        actionThree()
    }

    fun testError() {
        setState {
            error("A")
        }
        execute {
            delay(1000)
            errorFromSuspend()
            error("B")
        }
    }

    private suspend fun errorFromSuspend() {
        delay(1)
        error("Error from suspend function")
    }

    fun testPartialState() {
        setNestedState {
            it.copy(componentFirst = "ABC")
        }
        setState {
            copy(log = "${log}\n${nestedState.componentFirst}")
        }
    }

    fun testAsync() = execute {
        setAsync { Async.loading() }
        delay(1000)
        setAsync { Async.failure(Throwable("unknown error")) }
        delay(1000)
        setAsync { Async.success("Success") }
    }

    /* --------------------------------------------------- */
    /* > Private */
    /* --------------------------------------------------- */

    override fun KarmaContext<PlaygroundState>.onError(e: Exception) {
        addLog("Error happen: ${e.message}")
        setState {
            copy(log = "FROM ERRPR")
        }
    }

    private fun actionOne() = execute {
        addLog("before sleep")
        delay(1000)
        addLog("End of Action #1 | Before $this")
    }

    private fun actionTwo() = execute {
        delay(500)
        addLog("Action #2")
    }

    private fun actionThree() = addLog("Action #3 | Before $this")

    private fun addLog(newLog: String) {
        setState { copy(log = "${log}\n${newLog}") }
    }

}