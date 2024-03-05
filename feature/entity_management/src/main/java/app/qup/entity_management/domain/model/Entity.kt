package app.qup.entity_management.domain.model

import app.qup.entity_management.data.remote.dto.general.EntityAccoladeSet
import app.qup.entity_management.data.remote.dto.general.EntityAdminInfo
import app.qup.entity_management.data.remote.dto.general.EntityServiceSet
import app.qup.entity_management.data.remote.dto.general.EntitySpecialitySet
import app.qup.entity_management.data.remote.dto.general.EntityType
import app.qup.entity_management.data.remote.dto.general.FacilitySet
import app.qup.entity_management.data.remote.dto.general.GeoLocation
import app.qup.entity_management.data.remote.dto.general.InsuranceCompanySet
import app.qup.entity_management.data.remote.dto.general.LandlineNumber

data class Entity(
    val accountManagerIds: List<String>,
    val active: Boolean,
    val addressLine1: String,
    val addressLine2: String,
    val appDownloadLink: String,
    val appId: String,
    val area: String,
    val associatedDoctorSet: List<String>,
    val city: String,
    val country: String,
    val email: List<String>,
    val entityAccoladeSet: List<EntityAccoladeSet>,
    val entityAchievements: List<String>,
    val entityAdminInfo: EntityAdminInfo,
    val entityId: String,
    val entityServiceSet: List<EntityServiceSet>,
    val entitySpecialitySet: List<EntitySpecialitySet>,
    val entityType: EntityType,
    val facilitySet: List<FacilitySet>,
    val geoLocation: GeoLocation,
    val insuranceCompanySet: List<InsuranceCompanySet>,
    val landlineNumber: List<LandlineNumber>,
    val landmark: String,
    val mobileNumber: List<Long>,
    val name: String,
    val open24By7: Boolean,
    val pincode: String,
    val receptionistIds: List<String>,
    val registrationMonth: Int,
    val registrationNumber: String,
    val registrationYear: Int,
    val state: String,
    val virtualReceptionIds: List<String>,
    val website: String
)