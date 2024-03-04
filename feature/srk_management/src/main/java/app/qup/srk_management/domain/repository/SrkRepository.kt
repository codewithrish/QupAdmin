package app.qup.srk_management.domain.repository

import androidx.paging.PagingData
import app.qup.srk_management.data.remote.dto.general.SrkResource
import app.qup.srk_management.data.remote.dto.request.SearchUserRequestDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface SrkRepository {
    suspend fun getAllSrk(role: String, size: Int): Flow<PagingData<SrkResource>>
    suspend fun searchSrkByName(searchUserRequestDto: SearchUserRequestDto): Response<List<SrkResource>>
    suspend fun searchReceptionByNumber(role: String, mobileNumber: String): Response<SrkResource>
}