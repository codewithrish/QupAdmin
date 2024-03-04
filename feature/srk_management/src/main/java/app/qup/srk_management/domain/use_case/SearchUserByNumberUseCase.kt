package app.qup.srk_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.srk_management.data.remote.dto.general.toSrk
import app.qup.srk_management.domain.model.Srk
import app.qup.srk_management.domain.repository.SrkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchUserByNumberUseCase @Inject constructor(
    private val srkRepository: SrkRepository
) {
    operator fun invoke(
        role: String, mobileNumber: String
    ) = channelFlow {
        send(SearchUserByNumberState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                srkRepository.searchReceptionByNumber(role, mobileNumber).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                SearchUserByNumberState(
                                    isLoading = false,
                                    srk = it.body()?.toSrk()
                                )
                            )
                        } else {
                            send(
                                SearchUserByNumberState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(SearchUserByNumberState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class SearchUserByNumberState(
    val isLoading: Boolean = false, val srk: Srk? = null, val error: String? = ""
)