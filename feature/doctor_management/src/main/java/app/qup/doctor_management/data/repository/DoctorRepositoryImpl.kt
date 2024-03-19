package app.qup.doctor_management.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.qup.doctor_management.data.paging.GetAllDoctorsPagingData
import app.qup.doctor_management.data.paging.SearchDoctorsByNamePagingData
import app.qup.doctor_management.data.remote.api.DoctorApi
import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.data.remote.dto.general.UserResource
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.data.remote.dto.response.AccoladeResponseDto
import app.qup.doctor_management.data.remote.dto.response.DegreeResponseDto
import app.qup.doctor_management.data.remote.dto.response.DoctorResponseDto
import app.qup.doctor_management.data.remote.dto.response.SpecialityCategoryResponseDto
import app.qup.doctor_management.data.remote.dto.response.SpecialityResponseDto
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

    override suspend fun addDoctor(doctorRequestDto: DoctorRequestDto): Response<DoctorResponseDto> {
        return doctorApi.addDoctor(doctorRequestDto)
    }

    override suspend fun getGenders(): Response<List<String>> {
        return doctorApi.getGenders()
    }

    override suspend fun getActiveDegreeMaster(): Response<List<DegreeResponseDto>> {
        return doctorApi.getActiveDegreeMaster()
    }

    override suspend fun getActiveSpecialityMaster(): Response<List<SpecialityResponseDto>> {
        return doctorApi.getActiveSpecialityMaster()
    }

    override suspend fun getActiveSpecialityCategoryMaster(): Response<List<SpecialityCategoryResponseDto>> {
        return doctorApi.getActiveSpecialityCategoryMaster()
    }

    override suspend fun getActiveAccolades(): Response<List<AccoladeResponseDto>> {
        return doctorApi.getActiveAccolades()
    }

    override suspend fun getUserInfo(mobileNumber: String): Response<UserResource> {
        return doctorApi.getUserInfo(mobileNumber)
    }

    override suspend fun getDoctorById(id: String): Response<DoctorResponseDto> {
        return doctorApi.getDoctorById(id)
    }

    override suspend fun updateDoctorById(
        id: String,
        doctorRequestDto: DoctorRequestDto
    ): Response<DoctorResponseDto> {
        return doctorApi.updateDoctorById(id, doctorRequestDto)
    }
}