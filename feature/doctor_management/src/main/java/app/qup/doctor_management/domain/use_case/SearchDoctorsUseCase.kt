package app.qup.doctor_management.domain.use_case

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import app.qup.doctor_management.data.remote.dto.general.toDoctor
import app.qup.doctor_management.domain.model.Doctor
import app.qup.doctor_management.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchDoctorsUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    suspend operator fun invoke(doctorName: String, size: Int) : Flow<PagingData<Doctor>>
    = doctorRepository.searchDoctorByName(doctorName, size).map { pagingData ->
        pagingData.map { it1 ->
            Log.d("TAG", "invoke: ${it1.toDoctor()}")
            it1.toDoctor()
        }
    }
}