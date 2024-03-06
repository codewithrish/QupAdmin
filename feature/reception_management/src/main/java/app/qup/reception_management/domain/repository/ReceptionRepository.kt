package app.qup.reception_management.domain.repository

import androidx.paging.PagingData
import app.qup.reception_management.data.remote.dto.general.ReceptionResource
import app.qup.reception_management.data.remote.dto.request.AddReceptionRequestDto
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ReceptionRepository {
    suspend fun getAllReceptions(role: String, size: Int): Flow<PagingData<ReceptionResource>>
    suspend fun searchReceptionByName(searchUserRequestDto: SearchUserRequestDto): Response<List<ReceptionResource>>
    suspend fun searchReceptionByNumber(role: String, mobileNumber: String): Response<ReceptionResource>
    suspend fun addReception(addReceptionRequestDto: AddReceptionRequestDto): Response<ReceptionResource>
    suspend fun getUserByMobileNumber(mobileNumber: String): Response<ReceptionResource>
    suspend fun updateUser(mobileNumber: String,addReceptionRequestDto: AddReceptionRequestDto): Response<ReceptionResource>
    suspend fun getGenders(): Response<List<String>>
    suspend fun checkRoleEligibility(mobileNumber: String, role: String): Response<ReceptionResource>
}