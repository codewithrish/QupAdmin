package app.qup.doctor_management.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.qup.doctor_management.data.paging.GetAllDoctorsPagingData
import app.qup.doctor_management.data.paging.SearchDoctorsByNamePagingData
import app.qup.doctor_management.data.remote.api.DoctorApi
import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val doctorApi: DoctorApi
) : DoctorRepository {
    override suspend fun getAllDoctors(size: Int): Flow<PagingData<DoctorSearchResource>> = Pager(
        config = PagingConfig(pageSize = size, maxSize = size * 4),
        pagingSourceFactory = {
            GetAllDoctorsPagingData(
                doctorApi,
                size
            )
        }
    ).flow

    override suspend fun searchDoctorByName(
        doctorName: String,
        size: Int
    ): Flow<PagingData<DoctorSearchResource>> = Pager(
        config = PagingConfig(pageSize = size, maxSize = size * 4),
        pagingSourceFactory = {
            SearchDoctorsByNamePagingData(
                doctorApi,
                doctorName,
                size
            )
        }
    ).flow

    override suspend fun searchDoctorByNumber(mobileNumber: String): Response<DoctorSearchResource> {
        return doctorApi.searchDoctorByNumber(mobileNumber)
    }
}