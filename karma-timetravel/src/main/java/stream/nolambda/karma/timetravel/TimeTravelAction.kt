package stream.nolambda.karma.timetravel

interface TimeTravelAction {
    fun forward()
    fun back()
    fun select(state: Any)
    fun replace(oldState: Any, newState: Any)

    fun getTimeline(): List<Any>

    val name: String
}