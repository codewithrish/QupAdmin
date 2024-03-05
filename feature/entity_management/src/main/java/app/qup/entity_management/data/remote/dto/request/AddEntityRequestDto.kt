package app.qup.entity_management.data.remote.dto.request

import app.qup.entity_management.data.remote.dto.general.EntityAccoladeSet
import app.qup.entity_management.data.remote.dto.general.EntityAdminInfo
import app.qup.entity_management.data.remote.dto.general.EntityServiceSet
import app.qup.entity_management.data.remote.dto.general.EntitySpecialitySet
import app.qup.entity_management.data.remote.dto.general.EntityType
import app.qup.entity_management.data.remote.dto.general.FacilitySet
import app.qup.entity_management.data.remote.dto.general.InsuranceCompanySet
import app.qup.entity_management.data.remote.dto.general.LandlineNumber

data class AddEntityRequestDto(
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val area: String? = null,
    val city: String? = null,
    val country: String? = null,
    val email: List<String>? = null,
    val entityAccoladeSet: List<EntityAccoladeSet>? = null,
    val entityAchievements: List<String>? = null,
    val entityAdminInfo: EntityAdminInfo? = null,
    val entityServiceSet: List<EntityServiceSet>? = null,
    val entitySpecialitySet: List<EntitySpecialitySet>? = null,
    val entityType: EntityType? = null,
    val facilitySet: List<FacilitySet>? = null,
    val insuranceCompanySet: List<InsuranceCompanySet>? = null,
    val landlineNumber: List<LandlineNumber>? = null,
    val landmark: String? = null,
    val mobileNumber: List<Long>? = null,
    val name: String? = null,
    val open24By7: Boolean? = null,
    val pincode: String? = null,
    val registrationMonth: Int? = null,
    val registrationNumber: String? = null,
    val registrationYear: Int? = null,
    val state: String? = null,
    val website: String? = null
)