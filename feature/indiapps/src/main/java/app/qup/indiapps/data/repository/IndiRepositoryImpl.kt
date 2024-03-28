package app.qup.indiapps.data.repository

import app.qup.indiapps.data.remote.api.IndiApi
import app.qup.indiapps.data.remote.dto.response.IndiAppResponseDto
import app.qup.indiapps.domain.repository.IndiRepository
import retrofit2.Response
import javax.inject.Inject

class IndiRepositoryImpl @Inject constructor(
    private val indiApi: IndiApi
) : IndiRepository {
    override suspend fun getIndiApps(): Response<List<IndiAppResponseDto>> {
        return indiApi.getIndiApps()
    }
}