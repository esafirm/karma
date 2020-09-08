package nolambda.github.usersearch.utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.call(
    onComplete: () -> Unit = {},
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit = {}
) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            onError(t)
            onComplete()
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    onSuccess(body)
                } else {
                    onError(ApiErrorException("Body is empty"))
                }
            } else {
                onError(ApiErrorException(response.message()))
            }
            onComplete()
        }
    })
}

class ApiErrorException(
    override val message: String
) : Exception(message)