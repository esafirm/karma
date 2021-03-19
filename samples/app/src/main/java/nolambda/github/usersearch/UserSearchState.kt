package nolambda.github.usersearch

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nolambda.github.usersearch.data.User
import stream.nolambda.karma.differ.Event

@Serializable
data class UserSearchState(
    val lastQuery: String = "",
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    @Transient val err: Event<Throwable>? = null,
    val currentPage: Int = 1
)
