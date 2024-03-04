package app.qup.reception_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.reception_management.data.remote.dto.general.toReception
import app.qup.reception_management.domain.model.Reception
import app.qup.reception_management.domain.repository.ReceptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchUserByNumberUseCase @Inject constructor(
    private val receptionRepository: ReceptionRepository
) {
    operator fun invoke(
        role: String,
        mobileNumber: String
    ) = channelFlow {
        send(SearchUserByNumberState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                receptionRepository.searchReceptionByNumber(role, mobileNumber).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(SearchUserByNumberState(isLoading = false, reception = it.body()?.toReception()))
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
    val isLoading: Boolean = false,
    val reception: Reception? = null,
    val error: String? = ""
)