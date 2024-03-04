package app.qup.reception_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.reception_management.data.remote.dto.general.toReception
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.reception_management.domain.model.Reception
import app.qup.reception_management.domain.repository.ReceptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchUserByNameUseCase @Inject constructor(
    private val receptionRepository: ReceptionRepository
) {
    operator fun invoke(
        searchUserRequestDto: SearchUserRequestDto
    ) = channelFlow {
        send(SearchUserByNameState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                receptionRepository.searchReceptionByName(searchUserRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(SearchUserByNameState(isLoading = false, receptions = it.body()?.map { it1 -> it1.toReception() }))
                        } else {
                            send(
                                SearchUserByNameState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(SearchUserByNameState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class SearchUserByNameState(
    val isLoading: Boolean = false,
    val receptions: List<Reception>? = null,
    val error: String? = ""
)