package stream.nolambda.karma.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

interface ActionExecutor {
    fun execute(action: () -> Unit)
    fun launch(action: suspend () -> Unit)
}

class TestActionExecutor : ActionExecutor {
    override fun execute(action: () -> Unit) {
        action.invoke()
    }

    override fun launch(action: suspend () -> Unit) {
        runBlocking {
            action.invoke()
        }
    }
}

class DefaultActionExecutor : ActionExecutor {

    private val executor = Executors.newSingleThreadExecutor()
    private val executorScope = CoroutineScope(executor.asCoroutineDispatcher())

    override fun execute(action: () -> Unit) {
        executor.execute(action)
    }

    override fun launch(action: suspend () -> Unit) {
        executorScope.launch {
            action()
        }
    }
}