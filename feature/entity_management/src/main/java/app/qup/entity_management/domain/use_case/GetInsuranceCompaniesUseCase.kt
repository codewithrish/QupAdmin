package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.general.InsuranceCompanySet
import app.qup.entity_management.data.remote.dto.response.toInsuranceCompanySet
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GetInsuranceCompaniesUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
    ) = channelFlow {
        send(InsuranceCompaniesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.getInsuranceCompanies().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                InsuranceCompaniesState(
                                    isLoading = false,
                                    insuranceCompanies = it.body()?.map { it1 -> it1.toInsuranceCompanySet() }
                                )
                            )
                        } else {
                            send(
                                InsuranceCompaniesState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(InsuranceCompaniesState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class InsuranceCompaniesState(
    val isLoading: Boolean = false, val insuranceCompanies: List<InsuranceCompanySet>? = null, val error: String? = ""
)