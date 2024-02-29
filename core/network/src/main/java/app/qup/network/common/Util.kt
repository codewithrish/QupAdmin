package app.qup.network.common

import app.qup.network.data.remote.dto.ErrorResponseDto
import com.google.gson.Gson
import okhttp3.ResponseBody

fun parseErrorResponse(errorBody: ResponseBody?): String {
    val parsedError = Gson().fromJson(
        errorBody?.charStream(),
        ErrorResponseDto::class.java
    )
    return parsedError.error_description.substring(8, parsedError.error_description.lastIndex + 1)
}