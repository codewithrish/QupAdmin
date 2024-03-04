package app.qup.srk_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.srk_management.data.remote.dto.general.toSrk
import app.qup.srk_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.srk_management.domain.model.Srk
import app.qup.srk_management.domain.repository.SrkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchUserByNameUseCase @Inject constructor(
    private val srkRepository: SrkRepository
) {
    operator fun invoke(
        searchUserRequestDto: SearchUserRequestDto
    ) = channelFlow {
        send(SearchUserByNameState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                srkRepository.searchSrkByName(searchUserRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                SearchUserByNameState(
                                    isLoading = false,
                                    srks = it.body()?.map { it1 -> it1.toSrk() })
                            )
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
    val isLoading: Boolean = false, val srks: List<Srk>? = null, val error: String? = ""
)