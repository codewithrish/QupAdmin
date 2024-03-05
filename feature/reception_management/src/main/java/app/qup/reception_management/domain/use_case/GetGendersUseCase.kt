package app.qup.reception_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.reception_management.domain.repository.ReceptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGendersUseCase @Inject constructor(
    private val receptionRepository: ReceptionRepository
) {
    operator fun invoke() = channelFlow {
        send(GetGendersState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                receptionRepository.getGenders().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                GetGendersState(
                                    isLoading = false,
                                    genders = it.body() ?: mutableListOf()
                                )
                            )
                        } else {
                            send(
                                GetGendersState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(GetGendersState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class GetGendersState(
    val isLoading: Boolean = false, val genders: List<String>? = null, val error: String? = ""
)