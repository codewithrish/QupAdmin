package app.qup.reception_management.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.qup.reception_management.data.paging.GetAllReceptionsPagingData
import app.qup.reception_management.data.remote.api.ReceptionApi
import app.qup.reception_management.data.remote.dto.general.ReceptionResource
import app.qup.reception_management.data.remote.dto.request.AddReceptionRequestDto
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.reception_management.domain.repository.ReceptionRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ReceptionRepositoryImpl @Inject constructor(
    private val receptionApi: ReceptionApi
) : ReceptionRepository {
    override suspend fun getAllReceptions(
        role: String,
        size: Int
    ): Flow<PagingData<ReceptionResource>> = Pager(
        config = PagingConfig(pageSize = size, maxSize = size * 4),
        pagingSourceFactory = {
            GetAllReceptionsPagingData(receptionApi, role, size)
        }
    ).flow

    override suspend fun searchReceptionByName(searchUserRequestDto: SearchUserRequestDto): Response<List<ReceptionResource>> {
        return receptionApi.searchReceptionByName(searchUserRequestDto)
    }

    override suspend fun searchReceptionByNumber(
        role: String,
        mobileNumber: String
    ): Response<ReceptionResource> {
        return receptionApi.searchReceptionByNumber(role, mobileNumber)
    }

    override suspend fun addReception(addReceptionRequestDto: AddReceptionRequestDto): Response<ReceptionResource> {
        return receptionApi.addReception(addReceptionRequestDto)
    }

    override suspend fun getUserByMobileNumber(mobileNumber: String): Response<ReceptionResource> {
        return receptionApi.getUserByMobileNumber(mobileNumber)
    }

    override suspend fun updateUser(
        mobileNumber: String,
        addReceptionRequestDto: AddReceptionRequestDto
    ): Response<ReceptionResource> {
        return receptionApi.updateUser(mobileNumber, addReceptionRequestDto)
    }

    override suspend fun getGenders(): Response<List<String>> {
        return receptionApi.getGenders()
    }

    override suspend fun checkRoleEligibility(
        mobileNumber: String,
        role: String
    ): Response<ReceptionResource> {
        return receptionApi.checkRoleEligibility(mobileNumber, role)
    }
}