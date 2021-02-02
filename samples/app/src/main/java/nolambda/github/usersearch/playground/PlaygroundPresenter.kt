package nolambda.github.usersearch.playground

import stream.nolambda.karma.ui.UiPresenter

class PlaygroundPresenter : UiPresenter<String>() {
    override fun initialState(): String = ""

    fun actionOne() = execute {
        set { "Start of Action #1" }
        Thread.sleep(10_000)
        set { "End of Action #1" }
    }

    fun actionTwo() = execute {
        set { "Start of Action #2" }
        Thread.sleep(500)
        set { "End of Action #2" }
    }
}