package nolambda.github.usersearch

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nolambda.github.usersearch.data.User
import stream.nolambda.karma.differ.SingleEvent

@Serializable
data class UserSearchState(
    val lastQuery: String = "",
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    @Transient val err: SingleEvent<Throwable>? = null,
    val currentPage: Int = 1
)
