package stream.nolambda.karma.observer

interface StateObserverFactory {
    fun <T> getStateObserver(isTest: Boolean): StateObserver<T>
}

class DefaultStateObserverFactory : StateObserverFactory {
    override fun <T> getStateObserver(isTest: Boolean): StateObserver<T> {
        return if (isTest) {
            InstantStateObserver()
        } else {
            LiveDataStateObserver()
        }
    }
}