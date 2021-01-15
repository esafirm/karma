package stream.nolambda.karma.timetravel.dashboard.serializer

interface StateSerializer {
    fun toString(state: Any): String
    fun fromString(stateString: String, clazz: Class<*>): Any
}