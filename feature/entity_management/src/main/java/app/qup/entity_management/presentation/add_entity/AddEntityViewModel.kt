package app.qup.entity_management.presentation.add_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.entity_management.data.remote.dto.request.EntityRequestDto
import app.qup.entity_management.domain.use_case.AccoladesState
import app.qup.entity_management.domain.use_case.AddEntityState
import app.qup.entity_management.domain.use_case.AddEntityUseCase
import app.qup.entity_management.domain.use_case.EntityTypesState
import app.qup.entity_management.domain.use_case.FacilitiesState
import app.qup.entity_management.domain.use_case.GetAccoladesUseCase
import app.qup.entity_management.domain.use_case.GetEntityByIdState
import app.qup.entity_management.domain.use_case.GetEntityByIdUseCase
import app.qup.entity_management.domain.use_case.GetEntityTypesUseCase
import app.qup.entity_management.domain.use_case.GetFacilitiesUseCase
import app.qup.entity_management.domain.use_case.GetInsuranceCompaniesUseCase
import app.qup.entity_management.domain.use_case.GetServicesUseCase
import app.qup.entity_management.domain.use_case.GetSpecialitiesUseCase
import app.qup.entity_management.domain.use_case.InsuranceCompaniesState
import app.qup.entity_management.domain.use_case.ServicesState
import app.qup.entity_management.domain.use_case.SpecialitiesState
import app.qup.entity_management.domain.use_case.UpdateEntityByIdState
import app.qup.entity_management.domain.use_case.UpdateEntityByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddEntityViewModel @Inject constructor(
    private val addEntityUseCase: AddEntityUseCase,
    private val getEntityTypesUseCase: GetEntityTypesUseCase,
    private val getAccoladesUseCase: GetAccoladesUseCase,
    private val getFacilitiesUseCase: GetFacilitiesUseCase,
    private val getInsuranceCompaniesUseCase: GetInsuranceCompaniesUseCase,
    private val getServicesUseCase: GetServicesUseCase,
    private val getSpecialitiesUseCase: GetSpecialitiesUseCase,
    private val getEntityByIdUseCase: GetEntityByIdUseCase,
    private val updateEntityByIdUseCase: UpdateEntityByIdUseCase

) : ViewModel() {
    val stepNumber: MutableLiveData<Int> = MutableLiveData(1)

    private val _addEntity: MutableLiveData<AddEntityState> = MutableLiveData()
    val addEntity: LiveData<AddEntityState>
        get() = _addEntity

    private val _getEntity: MutableLiveData<GetEntityByIdState> = MutableLiveData()
    val getEntity: LiveData<GetEntityByIdState>
        get() = _getEntity

    private val _updateEntity: MutableLiveData<UpdateEntityByIdState> = MutableLiveData()
    val updateEntity: LiveData<UpdateEntityByIdState>
        get() = _updateEntity

    private val _entityTypes: MutableLiveData<EntityTypesState> = MutableLiveData()
    val entityTypes: LiveData<EntityTypesState>
        get() = _entityTypes

    private val _accolades: MutableLiveData<AccoladesState> = MutableLiveData()
    val accolades: LiveData<AccoladesState>
        get() = _accolades

    private val _facilities: MutableLiveData<FacilitiesState> = MutableLiveData()
    val facilities: LiveData<FacilitiesState>
        get() = _facilities

    private val _insuranceCompanies: MutableLiveData<InsuranceCompaniesState> = MutableLiveData()
    val insuranceCompanies: LiveData<InsuranceCompaniesState>
        get() = _insuranceCompanies

    private val _entityServices: MutableLiveData<ServicesState> = MutableLiveData()
    val entityServices: LiveData<ServicesState>
        get() = _entityServices

    private val _entitySpecialities: MutableLiveData<SpecialitiesState> = MutableLiveData()
    val entitySpecialities: LiveData<SpecialitiesState>
        get() = _entitySpecialities

    init {

    }

    fun addEntity(
        entityRequestDto: EntityRequestDto
    ) {
        addEntityUseCase(entityRequestDto).onEach {
            _addEntity.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getEntityById(
        id: String,
    ) {
        getEntityByIdUseCase(id).onEach {
            _getEntity.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun updateEntityById(
        id: String,
        entityRequestDto: EntityRequestDto
    ) {
        updateEntityByIdUseCase(id, entityRequestDto).onEach {
            _updateEntity.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getEntityTypes() {
        getEntityTypesUseCase().onEach {
            _entityTypes.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getAccolades() {
        getAccoladesUseCase().onEach {
            _accolades.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getFacilities() {
        getFacilitiesUseCase().onEach {
            _facilities.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getInsuranceCompanies() {
        getInsuranceCompaniesUseCase().onEach {
            _insuranceCompanies.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getEntityServices() {
        getServicesUseCase().onEach {
            _entityServices.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getEntitySpecialities() {
        getSpecialitiesUseCase().onEach {
            _entitySpecialities.postValue(it)
        }.launchIn(viewModelScope)
    }
}