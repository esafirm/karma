package nolambda.github.usersearch.data


import com.google.gson.annotations.SerializedName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("login")
    val login: String
) : Serializable