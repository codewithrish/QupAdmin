package app.qup.indiapps.domain.repository

import app.qup.indiapps.data.remote.dto.response.IndiAppResponseDto
import retrofit2.Response

interface IndiRepository {
    suspend fun getIndiApps(): Response<List<IndiAppResponseDto>>
}