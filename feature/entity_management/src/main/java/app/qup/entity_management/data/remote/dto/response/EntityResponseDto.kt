package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.EntityAccoladeSet
import app.qup.entity_management.data.remote.dto.general.EntityAdminInfo
import app.qup.entity_management.data.remote.dto.general.EntityServiceSet
import app.qup.entity_management.data.remote.dto.general.EntitySpecialitySet
import app.qup.entity_management.data.remote.dto.general.EntityType
import app.qup.entity_management.data.remote.dto.general.FacilitySet
import app.qup.entity_management.data.remote.dto.general.GeoLocation
import app.qup.entity_management.data.remote.dto.general.InsuranceCompanySet
import app.qup.entity_management.data.remote.dto.general.LandlineNumber
import app.qup.entity_management.data.remote.dto.general.Link
import app.qup.entity_management.data.remote.dto.request.EntityRequestDto
import app.qup.entity_management.domain.model.Entity

data class EntityResponseDto(
    val accountManagerIds: List<String>?,
    val active: Boolean?,
    val addressLine1: String?,
    val addressLine2: String?,
    val appDownloadLink: String?,
    val appId: String?,
    val area: String?,
    val associatedDoctorSet: List<String>?,
    val city: String?,
    val country: String?,
    val email: List<String>?,
    val entityAccoladeSet: List<EntityAccoladeSet>?,
    val entityAchievements: List<String>?,
    val entityAdminInfo: EntityAdminInfo?,
    val entityId: String?,
    val entityServiceSet: List<EntityServiceSet>?,
    val entitySpecialitySet: List<EntitySpecialitySet>?,
    val entityType: EntityType?,
    val facilitySet: List<FacilitySet>?,
    val geoLocation: GeoLocation?,
    val insuranceCompanySet: List<InsuranceCompanySet>?,
    val landlineNumber: List<LandlineNumber>?,
    val landmark: String?,
    val links: List<Link>?,
    val mobileNumber: List<Long>?,
    val name: String?,
    val open24By7: Boolean?,
    val pincode: String?,
    val receptionistIds: List<String>?,
    val registrationMonth: Int?,
    val registrationNumber: String?,
    val registrationYear: Int?,
    val state: String?,
    val virtualReceptionIds: List<String>?,
    val website: String?
)

fun EntityResponseDto.toEntity(): Entity {
    return Entity(
        accountManagerIds = accountManagerIds ?: mutableListOf(),
        active = active ?: false,
        addressLine1 = addressLine1 ?: "",
        addressLine2 = addressLine2 ?: "",
        appDownloadLink = appDownloadLink ?: "",
        appId = appId ?: "",
        area = area ?: "",
        associatedDoctorSet = associatedDoctorSet ?: mutableListOf(),
        city = city ?: "",
        country = country ?: "",
        email = email ?: mutableListOf(),
        entityAccoladeSet = entityAccoladeSet ?: mutableListOf(),
        entityAchievements = entityAchievements ?: mutableListOf(),
        entityAdminInfo = entityAdminInfo ?: EntityAdminInfo(),
        entityId = entityId ?: "",
        entityServiceSet = entityServiceSet ?: mutableListOf(),
        entitySpecialitySet = entitySpecialitySet ?: mutableListOf(),
        entityType = entityType ?: EntityType(),
        facilitySet = facilitySet ?: mutableListOf(),
        geoLocation = geoLocation ?: GeoLocation(),
        insuranceCompanySet = insuranceCompanySet ?: mutableListOf(),
        landlineNumber = landlineNumber ?: mutableListOf(),
        landmark = landmark ?: "",
        mobileNumber = mobileNumber ?: mutableListOf(),
        name = name ?: "",
        open24By7 = open24By7 ?: false,
        pincode = pincode ?: "",
        receptionistIds = receptionistIds ?: mutableListOf(),
        registrationMonth = registrationMonth ?: 0,
        registrationNumber = registrationNumber ?: "",
        registrationYear = registrationYear ?: 0,
        state = state ?: "",
        virtualReceptionIds = virtualReceptionIds ?: mutableListOf(),
        website = website ?: ""
    )
}

fun EntityResponseDto.toEntityRequestDto(): EntityRequestDto {
    return EntityRequestDto(
        // Step 1
        entityType = entityType,
        name = name,
        registrationNumber = registrationNumber,
        registrationYear = registrationYear,
        registrationMonth = registrationMonth,
        open24By7 = open24By7,
        website = website,
        // Step 2
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        pincode = pincode,
        state = state,
        city = city,
        area = area,
        landmark = landmark,
        country = country,
        // Step 3
        mobileNumber = mobileNumber,
        landlineNumber = landlineNumber,
        email = email,
        insuranceCompanySet = insuranceCompanySet,
        entityAdminInfo = entityAdminInfo,
        // Step 4
        facilitySet = facilitySet,
        entitySpecialitySet = entitySpecialitySet,
        entityServiceSet = entityServiceSet,
        entityAccoladeSet = entityAccoladeSet,
        entityAchievements = entityAchievements
    )
}