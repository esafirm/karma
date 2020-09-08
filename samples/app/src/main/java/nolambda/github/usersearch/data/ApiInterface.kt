package nolambda.github.usersearch.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @GET("search/users")
    @Headers("Content-Type: application/json")
    fun search(
        @Query("q") q: String,
        @Query("page") page: Int
    ): Call<ApiResponse<User>>

}