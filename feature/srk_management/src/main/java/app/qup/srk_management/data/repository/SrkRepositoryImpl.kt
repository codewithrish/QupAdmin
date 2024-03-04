package app.qup.srk_management.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.qup.srk_management.data.paging.GetAllSrkPagingData
import app.qup.srk_management.data.remote.api.SrkApi
import app.qup.srk_management.data.remote.dto.general.SrkResource
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

    override suspend fun searchReceptionByNumber(
        role: String,
        mobileNumber: String
    ): Response<SrkResource> {
        return srkApi.searchReceptionByNumber(role, mobileNumber)
    }
}