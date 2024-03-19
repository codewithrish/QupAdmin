package app.qup.doctor_management.domain.repository

import androidx.paging.PagingData
import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.data.remote.dto.general.UserResource
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.data.remote.dto.response.AccoladeResponseDto
import app.qup.doctor_management.data.remote.dto.response.DegreeResponseDto
import app.qup.doctor_management.data.remote.dto.response.DoctorResponseDto
import app.qup.doctor_management.data.remote.dto.response.SpecialityCategoryResponseDto
import app.qup.doctor_management.data.remote.dto.response.SpecialityResponseDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DoctorRepository {
    suspend fun getAllDoctors(size: Int): Flow<PagingData<DoctorSearchResource>>
    suspend fun searchDoctorByName(doctorName: String, size: Int): Flow<PagingData<DoctorSearchResource>>
    suspend fun searchDoctorByNumber(mobileNumber: String): Response<DoctorSearchResource>
    suspend fun addDoctor(doctorRequestDto: DoctorRequestDto): Response<DoctorResponseDto>
    suspend fun getGenders(): Response<List<String>>
    suspend fun getActiveDegreeMaster(): Response<List<DegreeResponseDto>>
    suspend fun getActiveSpecialityMaster(): Response<List<SpecialityResponseDto>>
    suspend fun getActiveSpecialityCategoryMaster(): Response<List<SpecialityCategoryResponseDto>>
    suspend fun getActiveAccolades(): Response<List<AccoladeResponseDto>>
    suspend fun getUserInfo(mobileNumber: String): Response<UserResource>
    suspend fun getDoctorById(id: String): Response<DoctorResponseDto>
    suspend fun updateDoctorById(id: String,doctorRequestDto: DoctorRequestDto): Response<DoctorResponseDto>
}