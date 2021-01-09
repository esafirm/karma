package stream.nolambda.karma.timetravel.dashboard.serializer

import com.google.gson.GsonBuilder

class GsonStateSerializer : StateSerializer {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun toString(state: Any): String = gson.toJson(state)

    override fun fromString(stateString: String, clazz: Class<*>): Any =
        gson.fromJson(stateString, clazz)
}