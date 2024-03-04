package app.qup.srk_management.domain.use_case

import androidx.paging.PagingData
import androidx.paging.map
import app.qup.srk_management.data.remote.dto.general.toSrk
import app.qup.srk_management.domain.model.Srk
import app.qup.srk_management.domain.repository.SrkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllSrkUseCase @Inject constructor(
    private val srkRepository: SrkRepository
) {
    suspend operator fun invoke(role: String, size: Int) : Flow<PagingData<Srk>>
    = srkRepository.getAllSrk(role, size).map { pagingData ->
        pagingData.map { it1 ->
            it1.toSrk()
        }
    }
}