package app.qup.doctor_management.domain.repository

import androidx.paging.PagingData
import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.data.remote.dto.response.DoctorResponseDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DoctorRepository {
    suspend fun getAllDoctors(size: Int): Flow<PagingData<DoctorSearchResource>>
    suspend fun searchDoctorByName(doctorName: String, size: Int): Flow<PagingData<DoctorSearchResource>>
    suspend fun searchDoctorByNumber(mobileNumber: String): Response<DoctorSearchResource>
    suspend fun addDoctor(doctorRequestDto: DoctorRequestDto): Response<DoctorResponseDto>
    suspend fun getGenders(): Response<List<String>>
}