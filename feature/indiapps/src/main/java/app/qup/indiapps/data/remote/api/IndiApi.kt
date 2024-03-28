package app.qup.indiapps.data.remote.api

import app.qup.indiapps.data.remote.dto.response.IndiAppResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface IndiApi {
    @GET("provider-service/indi-clinic/")
    suspend fun getIndiApps(): Response<List<IndiAppResponseDto>>
}