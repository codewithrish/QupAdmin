package app.qup.srk_management.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.qup.srk_management.data.paging.GetAllSrkPagingData
import app.qup.srk_management.data.remote.api.SrkApi
import app.qup.srk_management.data.remote.dto.general.SrkResource
import app.qup.srk_management.data.remote.dto.request.AddSrkRequestDto
import app.qup.srk_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.srk_management.domain.repository.SrkRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class SrkRepositoryImpl @Inject constructor(
    private val srkApi: SrkApi
) : SrkRepository {
    override suspend fun getAllSrk(role: String, size: Int): Flow<PagingData<SrkResource>> = Pager(
        config = PagingConfig(pageSize = size, maxSize = size * 4),
        pagingSourceFactory = {
            GetAllSrkPagingData(srkApi, role, size)
        }
    ).flow

    override suspend fun searchSrkByName(searchUserRequestDto: SearchUserRequestDto): Response<List<SrkResource>> {
        return srkApi.searchSrkByName(searchUserRequestDto)
    }

    override suspend fun searchSrkByNumber(
        role: String,
        mobileNumber: String
    ): Response<SrkResource> {
        return srkApi.searchSrkByNumber(role, mobileNumber)
    }

    override suspend fun addSrk(addSrkRequestDto: AddSrkRequestDto): Response<SrkResource> {
        return srkApi.addSrk(addSrkRequestDto)
    }

    override suspend fun getUserByMobileNumber(mobileNumber: String): Response<SrkResource> {
        return srkApi.getUserByMobileNumber(mobileNumber)
    }

    override suspend fun updateUser(
        mobileNumber: String,
        addSrkRequestDto: AddSrkRequestDto
    ): Response<SrkResource> {
        return srkApi.updateUser(mobileNumber, addSrkRequestDto)
    }

    override suspend fun getGenders(): Response<List<String>> {
        return srkApi.getGenders()
    }

    override suspend fun checkRoleEligibility(
        mobileNumber: String,
        role: String
    ): Response<SrkResource> {
        return srkApi.checkRoleEligibility(mobileNumber, role)
    }
}