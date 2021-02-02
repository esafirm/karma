package stream.nolambda.karma.config

interface ActionExecutorFactory {
    fun getExecutor(isTest: Boolean): ActionExecutor
}

class DefaultActionExecutorFactory : ActionExecutorFactory {
    override fun getExecutor(isTest: Boolean): ActionExecutor {
        return if (isTest) {
            TestActionExecutor()
        } else {
            DefaultActionExecutor()
        }
    }
}
