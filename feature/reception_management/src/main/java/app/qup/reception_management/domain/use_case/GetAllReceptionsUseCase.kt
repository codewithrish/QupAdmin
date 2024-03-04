package app.qup.reception_management.domain.use_case

import androidx.paging.PagingData
import androidx.paging.map
import app.qup.reception_management.data.remote.dto.general.toReception
import app.qup.reception_management.domain.model.Reception
import app.qup.reception_management.domain.repository.ReceptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllReceptionsUseCase @Inject constructor(
    private val receptionRepository: ReceptionRepository
) {
    suspend operator fun invoke(role: String, size: Int) : Flow<PagingData<Reception>>
    = receptionRepository.getAllReceptions(role, size).map { pagingData ->
        pagingData.map { it1 ->
            it1.toReception()
        }
    }
}