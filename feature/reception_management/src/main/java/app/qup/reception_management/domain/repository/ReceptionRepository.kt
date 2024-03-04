package app.qup.reception_management.domain.repository

import androidx.paging.PagingData
import app.qup.reception_management.data.remote.dto.general.ReceptionResource
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ReceptionRepository {
    suspend fun getAllReceptions(role: String, size: Int): Flow<PagingData<ReceptionResource>>
    suspend fun searchReceptionByName(searchUserRequestDto: SearchUserRequestDto): Response<List<ReceptionResource>>
    suspend fun searchReceptionByNumber(role: String, mobileNumber: String): Response<ReceptionResource>
}