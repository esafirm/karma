package nolambda.github.usersearch.data

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("total_count")
    val totalCount: Int?,
    @SerializedName("items")
    val items: List<T>
)