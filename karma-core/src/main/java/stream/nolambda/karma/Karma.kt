package stream.nolambda.karma

import stream.nolambda.karma.config.ActionExecutor
import stream.nolambda.karma.config.DefaultActionExecutor

object Karma {
    var isTestMode: Boolean = false
    var executor: ActionExecutor = DefaultActionExecutor()
    var enableLog = false

    fun setTestMode() {
        isTestMode = true
    }
}