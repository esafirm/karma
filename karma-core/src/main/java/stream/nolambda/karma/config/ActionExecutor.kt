package stream.nolambda.karma.config

import java.util.concurrent.Executors

interface ActionExecutor {
    fun execute(action: () -> Unit)
}

class TestActionExecutor : ActionExecutor {
    override fun execute(action: () -> Unit) {
        action.invoke()
    }
}

class DefaultActionExecutor : ActionExecutor {

    private val executor = Executors.newSingleThreadExecutor()

    override fun execute(action: () -> Unit) {
        executor.execute(action)
    }
}