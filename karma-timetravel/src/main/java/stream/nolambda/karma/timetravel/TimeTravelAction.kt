package stream.nolambda.karma.timetravel

interface TimeTravelAction {
    fun forward()
    fun back()
    fun set(state: Any)

    fun getTimeline(): List<Any>

    val name: String
}