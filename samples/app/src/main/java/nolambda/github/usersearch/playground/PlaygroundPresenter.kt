package nolambda.github.usersearch.playground

import kotlinx.coroutines.delay
import stream.nolambda.karma.ui.UiPresenter

class PlaygroundPresenter : UiPresenter<String>() {
    override fun initialState(): String = ""

    fun actionOne() = execute {
        setState { "Start of Action #1" }
        Logger.log("before sleep")
        delay(1000)
        setState { "End of Action #1 | Before $this" }
    }

    fun actionTwo() = execute {
        delay(500)
        setState { "Action #2" }
    }

    fun actionThree() = setState {
        "Action #3 | Before $this"
    }
}