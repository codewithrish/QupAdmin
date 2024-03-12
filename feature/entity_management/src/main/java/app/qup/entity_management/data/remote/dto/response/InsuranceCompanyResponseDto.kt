package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.InsuranceCompanySet
import app.qup.entity_management.data.remote.dto.general.Link

data class InsuranceCompanyResponseDto(
    val active: Boolean?,
    val description: String?,
    val insuranceComapnyId: String?,
    val links: List<Link>?,
    val name: String?
)

fun InsuranceCompanyResponseDto.toInsuranceCompanySet(): InsuranceCompanySet {
    return InsuranceCompanySet(
        insuranceCompanyId = insuranceComapnyId,
        name = name
    )
}