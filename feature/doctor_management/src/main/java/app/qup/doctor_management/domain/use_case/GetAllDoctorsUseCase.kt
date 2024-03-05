package app.qup.doctor_management.domain.use_case

import androidx.paging.PagingData
import androidx.paging.map
import app.qup.doctor_management.data.remote.dto.general.toDoctor
import app.qup.doctor_management.domain.model.DoctorR
import app.qup.doctor_management.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllDoctorsUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    suspend operator fun invoke(size: Int) : Flow<PagingData<DoctorR>>
    = doctorRepository.getAllDoctors(size).map { pagingData ->
        pagingData.map { it1 ->
            it1.toDoctor()
        }
    }
}