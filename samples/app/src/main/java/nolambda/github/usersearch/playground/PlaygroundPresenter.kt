package nolambda.github.usersearch.playground

import kotlinx.coroutines.delay
import stream.nolambda.karma.ui.UiPresenter
import kotlin.random.Random

class PlaygroundPresenter : UiPresenter<String>() {
    override fun initialState(): String = ""

    fun actionOne() = runSuspend {
        setState { "Start of Action #1" }
        Logger.log("before sleep")
        delay(1000)
        val a = Random.nextInt()
        setState { "End of Action #1: $a" }
    }

    fun actionTwo() = runSuspend {
        delay(500)
        setState { "Action #2" }
    }
}