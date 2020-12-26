package stream.nolambda.karma.timetravel

interface TimeTravelAction {
    fun forward()
    fun back()
    fun getTimeline(): List<Any>
}