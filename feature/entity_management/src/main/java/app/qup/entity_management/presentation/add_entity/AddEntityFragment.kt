package app.qup.entity_management.presentation.add_entity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import app.qup.entity_management.data.remote.dto.general.EntityAccoladeSet
import app.qup.entity_management.data.remote.dto.general.EntityServiceSet
import app.qup.entity_management.data.remote.dto.general.EntitySpecialitySet
import app.qup.entity_management.data.remote.dto.general.EntityType
import app.qup.entity_management.data.remote.dto.general.FacilitySet
import app.qup.entity_management.data.remote.dto.general.InsuranceCompanySet
import app.qup.entity_management.data.remote.dto.general.LandlineNumber
import app.qup.entity_management.data.remote.dto.request.EntityRequestDto
import app.qup.ui.common.snack
import app.qup.util.common.isValidEmail
import com.codewithrish.entity_management.databinding.FragmentAddEntityBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddEntityFragment : Fragment(), MenuProvider {
    private var _binding: FragmentAddEntityBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    private val addEntityViewModel by viewModels<AddEntityViewModel>()

    private val args by navArgs<AddEntityFragmentArgs>()

    // Adapters
    private lateinit var mobileNoAdapter: MobileNoAdapter
    private lateinit var emailIdAdapter: EmailIdAdapter
    private lateinit var landlineAdapter: LandlineAdapter
    private lateinit var achievementAdapter: AchievementAdapter

    private lateinit var insuranceAdapter: InsuranceAdapter
    private lateinit var facilityAdapter: FacilityAdapter
    private lateinit var specialityAdapter: SpecialityAdapter
    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var accoladeAdapter: AccoladeAdapter

    // Adapters Data
    private val selectedMobileNumbers = mutableListOf(0L)
    private val selectedEmailIds = mutableListOf("")
    private val selectedLandlineNumbers = mutableListOf(LandlineNumber())
    private val selectedInsuranceCompanies = mutableListOf(InsuranceCompanySet(name = "Select Insurance"))
    private val selectedFacilities = mutableListOf(FacilitySet(name = "Select Facility"))
    private val selectedSpecialities = mutableListOf(EntitySpecialitySet(name = "Select Speciality"))
    private val selectedServices = mutableListOf(EntityServiceSet(name = "Select Service"))
    private val selectedAccolades = mutableListOf(EntityAccoladeSet(name = "Select Accolade"))
    private val selectedAchievements = mutableListOf("")

    private var selectedEntityType: EntityType? = null
    private var selectedRegYear: Int? = null
    private var selectedRegMonth: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEntityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        callFunctions()
    }

    private fun callFunctions() {
        stepNumberObserver()

        args.entityId?.let { entityId ->
            addEntityViewModel.getEntityById(entityId)
        } ?: run {
            getFormData()
        }

        entityDataObserver()

        // Step 1
        populateEntityTypes()
        populateYears()
        populateMonths()

        // Step 2

        // Step 3
        setupMobileNoAdapter()
        setupEmailIdAdapter()
        setupLandlineAdapter()
        insuranceObservers()
        // Step 4
        setupAchievementAdapter()
        facilitiesObserver()
        specialitiesObserver()
        servicesObserver()
        accoladesObserver()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun getFormData() {
        addEntityViewModel.getEntityTypes()
        addEntityViewModel.getAccolades()
        addEntityViewModel.getFacilities()
        addEntityViewModel.getInsuranceCompanies()
        addEntityViewModel.getEntityServices()
        addEntityViewModel.getEntitySpecialities()
    }

    private fun entityDataObserver() {
        addEntityViewModel.getEntity.observe(viewLifecycleOwner) {
            it.entity?.let { entity ->
                if (entity.mobileNumber.isNotEmpty()) selectedMobileNumbers.clear()
                selectedMobileNumbers.addAll(entity.mobileNumber)
                if (entity.entityId.isNotEmpty()) selectedEmailIds.clear()
                selectedEmailIds.addAll(entity.email)
                if (entity.landlineNumber.isNotEmpty()) selectedLandlineNumbers.clear()
                selectedLandlineNumbers.addAll(entity.landlineNumber)
                if (entity.insuranceCompanySet.isNotEmpty()) selectedInsuranceCompanies.clear()
                selectedInsuranceCompanies.addAll(entity.insuranceCompanySet)
                if (entity.facilitySet.isNotEmpty()) selectedFacilities.clear()
                selectedFacilities.addAll(entity.facilitySet)
                if (entity.entitySpecialitySet.isNotEmpty()) selectedSpecialities.clear()
                selectedSpecialities.addAll(entity.entitySpecialitySet)
                if (entity.entityServiceSet.isNotEmpty()) selectedServices.clear()
                selectedServices.addAll(entity.entityServiceSet)
                if (entity.entityAccoladeSet.isNotEmpty()) selectedAccolades.clear()
                selectedAccolades.addAll(entity.entityAccoladeSet)
                if (entity.entityAchievements.isNotEmpty()) selectedAchievements.clear()
                selectedAchievements.addAll(entity.entityAchievements)

                selectedEntityType = entity.entityType
                selectedRegYear = entity.registrationYear
                selectedRegMonth = entity.registrationMonth

                // Step 1
                binding.layoutStep1.etEntityName.setText(entity.name)
                binding.layoutStep1.etRegistrationNumber.setText(entity.registrationNumber)
                binding.layoutStep1.etWebsite.setText(entity.website)
                binding.layoutStep1.sw24by7.isChecked = entity.open24By7
                binding.layoutStep1.spYear.setSelection(getYearsTillDate().indexOf(entity.registrationYear.toString()), true)
                binding.layoutStep1.spMonth.setSelection(entity.registrationMonth, true)

                // Step 2
                binding.layoutStep2.etAddressLine1.setText(entity.addressLine1)
                binding.layoutStep2.etAddressLine2.setText(entity.addressLine2)
                binding.layoutStep2.etPincode.setText(entity.pincode)
                binding.layoutStep2.etCountry.setText(entity.country)
                binding.layoutStep2.etState.setText(entity.state)
                binding.layoutStep2.etCity.setText(entity.city)
                binding.layoutStep2.etArea.setText(entity.area)
                binding.layoutStep2.etLandmark.setText(entity.landmark)
                getFormData()
            }
        }
    }

    private fun addEntity() {
        // Step 1
        val entityName = binding.layoutStep1.etEntityName.text.toString()
        val registrationNumber = binding.layoutStep1.etRegistrationNumber.text.toString()
        val website = binding.layoutStep1.etWebsite.text.toString()
        // Step 2
        val addressLine1 = binding.layoutStep2.etAddressLine1.text.toString()
        val addressLine2 = binding.layoutStep2.etAddressLine2.text.toString()
        val pincode = binding.layoutStep2.etPincode.text.toString()
        val country = binding.layoutStep2.etCountry.text.toString()
        val state = binding.layoutStep2.etState.text.toString()
        val city = binding.layoutStep2.etCity.text.toString()
        val area = binding.layoutStep2.etArea.text.toString()
        val landmark = binding.layoutStep2.etLandmark.text.toString()

        val entityRequestDto = EntityRequestDto(
            // Step 1
            entityType = selectedEntityType,
            name = entityName,
            registrationNumber = registrationNumber,
            registrationYear = selectedRegYear,
            registrationMonth = selectedRegMonth,
            open24By7 = binding.layoutStep1.sw24by7.isChecked,
            website = website,
            // Step 2
            addressLine1 = addressLine1,
            addressLine2 = addressLine2.ifEmpty { null },
            pincode = pincode,
            state = state,
            city = city,
            area = area,
            landmark = landmark,
            country = country,
            // Step 3
            mobileNumber = selectedMobileNumbers.filter { it1 -> it1.toString().isNotEmpty() && it1.toString().length == 10 },
            landlineNumber = selectedLandlineNumbers.filter { it1 -> !it1.stdCode.isNullOrEmpty() || it1.landlineNumber != null && it1.landlineNumber != 0L },
            email = selectedEmailIds.filter { it1 -> it1.isValidEmail() },
            insuranceCompanySet = selectedInsuranceCompanies.filter { it1 -> it1.name != "Select Insurance" },
            // Step 4
            facilitySet = selectedFacilities.filter { it1 -> it1.name != "Select Facility" },
            entitySpecialitySet = selectedSpecialities.filter { it1 -> it1.name != "Select Speciality" },
            entityServiceSet = selectedServices.filter { it1 -> it1.name != "Select Service" },
            entityAccoladeSet = selectedAccolades.filter { it1 -> it1.name != "Select Accolade" },
            entityAchievements = selectedAchievements.filter { it1 -> it1.isNotEmpty() },
        )

        args.entityId?.let {
            addEntityViewModel.updateEntityById(
                id = args.entityId!!,
                entityRequestDto = entityRequestDto
            )
        } ?: run {
            addEntityViewModel.addEntity(
                entityRequestDto = entityRequestDto
            )
        }
        addEntityViewModel.addEntity.observe(viewLifecycleOwner) {
            it.entity?.let {
                navController?.popBackStack()
            }
            it.error?.let { error ->
                if (error.isNotEmpty()) {
                    binding.root.snack(error)
                }
            }
            binding.progressBar.isVisible = it.isLoading
            binding.btnNext.isEnabled = !it.isLoading
        }
        addEntityViewModel.updateEntity.observe(viewLifecycleOwner) {
            it.entity?.let {
                navController?.popBackStack()
            }
            it.error?.let { error ->
                if (error.isNotEmpty()) {
                    binding.root.snack(error)
                }
            }
            binding.progressBar.isVisible = it.isLoading
            binding.btnNext.isEnabled = !it.isLoading
        }
    }

    // Step 1
    private fun populateEntityTypes() {
        addEntityViewModel.entityTypes.observe(viewLifecycleOwner) {
            it.entityTypes?.let { entityTypes ->
                val finalEntityTypes: MutableList<EntityType> = entityTypes.toMutableList()
                finalEntityTypes.add(0, EntityType(type = "Select Type"))
                val entityTypesAdapter: ArrayAdapter<EntityType?> =
                    object : ArrayAdapter<EntityType?>(requireContext(), app.qup.ui.R.layout.list_item_array_adapter, finalEntityTypes.toList()) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.text = getItem(position)?.type
                            return view
                        }

                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.setPadding(16,16,16,16)
                            view.text = getItem(position)?.type
                            return view
                        }
                    }
                binding.layoutStep1.spEntityType.setAdapter(entityTypesAdapter)
                binding.layoutStep1.spEntityType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedEntityType = entityTypesAdapter.getItem(position) as EntityType
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
                if (selectedEntityType != null) {
                    binding.layoutStep1.spEntityType.setSelection(finalEntityTypes.indexOf(selectedEntityType), true)
                }
            }
        }
    }

    // Step 3
    private fun setupMobileNoAdapter() {
        mobileNoAdapter = MobileNoAdapter()
        binding.layoutStep3.rvMobileNumbers.adapter = mobileNoAdapter
        binding.layoutStep3.rvMobileNumbers.setItemViewCacheSize(1000)
        mobileNoAdapter.submitList(selectedMobileNumbers)
        mobileNoAdapter.onMobileTyping = { mobileNo, position ->
            selectedMobileNumbers[position] = mobileNo
        }

        binding.layoutStep3.btnAddMobileNo.setOnClickListener {
            selectedMobileNumbers.add(0L)
            mobileNoAdapter.submitList(selectedMobileNumbers)
            mobileNoAdapter.notifyItemRangeInserted(selectedMobileNumbers.lastIndex, selectedMobileNumbers.size)
        }
    }

    private fun setupEmailIdAdapter() {
        emailIdAdapter = EmailIdAdapter()
        binding.layoutStep3.rvEmails.adapter = emailIdAdapter
        binding.layoutStep3.rvEmails.setItemViewCacheSize(1000)
        emailIdAdapter.submitList(selectedEmailIds)
        emailIdAdapter.onEmailTyping = { emailId, position ->
            selectedEmailIds[position] = emailId
        }

        binding.layoutStep3.btnAddAddEmail.setOnClickListener {
            selectedEmailIds.add("")
            emailIdAdapter.submitList(selectedEmailIds)
            emailIdAdapter.notifyItemRangeInserted(selectedEmailIds.lastIndex, selectedEmailIds.size)
        }
    }

    private fun setupLandlineAdapter() {
        landlineAdapter = LandlineAdapter()
        binding.layoutStep3.rvLandlineNumbers.adapter = landlineAdapter
        binding.layoutStep3.rvLandlineNumbers.setItemViewCacheSize(1000)
        landlineAdapter.submitList(selectedLandlineNumbers)
        landlineAdapter.onStdCodeTyping = { stdCode, position ->
            selectedLandlineNumbers[position] = selectedLandlineNumbers[position].copy(
                stdCode = stdCode
            )
        }
        landlineAdapter.onLandlineTyping = { landlineNumber, position ->
            selectedLandlineNumbers[position] = selectedLandlineNumbers[position].copy(
                landlineNumber = landlineNumber
            )
        }

        binding.layoutStep3.btnAddLandlineNumber.setOnClickListener {
            selectedLandlineNumbers.add(LandlineNumber())
            landlineAdapter.submitList(selectedLandlineNumbers)
            landlineAdapter.notifyItemRangeInserted(selectedLandlineNumbers.lastIndex, selectedLandlineNumbers.size)
        }
    }

    private fun insuranceObservers() {
        addEntityViewModel.insuranceCompanies.observe(viewLifecycleOwner) {
            it.insuranceCompanies?.let { insuranceCompanies ->
                val finalInsuranceCompanies = insuranceCompanies.toMutableList()
                finalInsuranceCompanies.add(0, InsuranceCompanySet(name = "Select Insurance"))
                setupInsuranceAdapter(finalInsuranceCompanies)
            }
        }
    }

    private fun setupInsuranceAdapter(finalInsuranceCompanies: MutableList<InsuranceCompanySet>) {
        insuranceAdapter = InsuranceAdapter(finalInsuranceCompanies)
        binding.layoutStep3.rvInsuranceCompanies.adapter = insuranceAdapter
        binding.layoutStep3.rvInsuranceCompanies.setItemViewCacheSize(1000)
        insuranceAdapter.submitList(selectedInsuranceCompanies)
        insuranceAdapter.onInsuranceSelected = { insuranceCompany, position ->
            selectedInsuranceCompanies[position] = selectedInsuranceCompanies[position].copy(
                insuranceCompanyId = insuranceCompany.insuranceCompanyId,
                name = insuranceCompany.name
            )
        }

        binding.layoutStep3.btnAddCompany.setOnClickListener {
            selectedInsuranceCompanies.add(InsuranceCompanySet(name = "Select Insurance"))
            insuranceAdapter.submitList(selectedInsuranceCompanies)
            insuranceAdapter.notifyItemRangeInserted(selectedInsuranceCompanies.lastIndex, selectedInsuranceCompanies.size)
        }
    }

    // Step 4
    private fun setupAchievementAdapter() {
        achievementAdapter = AchievementAdapter()
        binding.layoutStep4.rvAchievements.adapter = achievementAdapter
        binding.layoutStep4.rvAchievements.setItemViewCacheSize(1000)
        achievementAdapter.submitList(selectedAchievements)
        achievementAdapter.onAchievementTyping = { achievement, position ->
            selectedAchievements[position] = achievement
        }

        binding.layoutStep4.btnAddAchievement.setOnClickListener {
            selectedAchievements.add("")
            achievementAdapter.submitList(selectedAchievements)
            achievementAdapter.notifyItemRangeInserted(selectedAchievements.lastIndex, selectedAchievements.size)
        }
    }

    private fun facilitiesObserver() {
        addEntityViewModel.facilities.observe(viewLifecycleOwner) {
            it.facilities?.let { facilities ->
                val finalFacilities = facilities.toMutableList()
                finalFacilities.add(0, FacilitySet(name = "Select Facility"))
                setupFacilitiesAdapter(finalFacilities)
            }
        }
    }

    private fun setupFacilitiesAdapter(finalFacilities: MutableList<FacilitySet>) {
        facilityAdapter = FacilityAdapter(finalFacilities)
        binding.layoutStep4.rvFacilities.adapter = facilityAdapter
        binding.layoutStep4.rvFacilities.setItemViewCacheSize(1000)
        facilityAdapter.submitList(selectedFacilities)
        facilityAdapter.onFacilitySelected = { facility, position ->
            selectedFacilities[position] = selectedFacilities[position].copy(
                facilityId = facility.facilityId,
                name = facility.name
            )
        }
        facilityAdapter.onFacilityDescType = { description, position ->
            selectedFacilities[position] = selectedFacilities[position].copy(
                description = description
            )
        }

        binding.layoutStep4.btnAddFacility.setOnClickListener {
            selectedFacilities.add(FacilitySet(name = "Select Facility"))
            facilityAdapter.submitList(selectedFacilities)
            facilityAdapter.notifyItemRangeInserted(selectedFacilities.lastIndex, selectedFacilities.size)
        }
    }

    private fun specialitiesObserver() {
        addEntityViewModel.entitySpecialities.observe(viewLifecycleOwner) {
            it.entitySpecialities?.let { entitySpecialities ->
                val finalEntitySpecialities = entitySpecialities.toMutableList()
                finalEntitySpecialities.add(0, EntitySpecialitySet(name = "Select Speciality"))
                setupSpecialitiesAdapter(finalEntitySpecialities)
            }
        }
    }

    private fun setupSpecialitiesAdapter(finalEntitySpecialities: MutableList<EntitySpecialitySet>) {
        specialityAdapter = SpecialityAdapter(finalEntitySpecialities)
        binding.layoutStep4.rvSpecialities.adapter = specialityAdapter
        binding.layoutStep4.rvSpecialities.setItemViewCacheSize(1000)
        specialityAdapter.submitList(selectedSpecialities)
        specialityAdapter.onSpecialitySelected = { speciality, position ->
            selectedSpecialities[position] = selectedSpecialities[position].copy(
                specialityId = speciality.specialityId,
                name = speciality.name
            )
        }

        binding.layoutStep4.btnAddSpeciality.setOnClickListener {
            selectedSpecialities.add(EntitySpecialitySet(name = "Select Speciality"))
            specialityAdapter.submitList(selectedSpecialities)
            specialityAdapter.notifyItemRangeInserted(selectedSpecialities.lastIndex, selectedSpecialities.size)
        }
    }

    private fun servicesObserver() {
        addEntityViewModel.entityServices.observe(viewLifecycleOwner) {
            it.entityServices?.let { entityServices ->
                val finalEntityServices = entityServices.toMutableList()
                finalEntityServices.add(0, EntityServiceSet(name = "Select Service"))
                setupServicesAdapter(finalEntityServices)
            }
        }
    }

    private fun setupServicesAdapter(finalEntityServices: MutableList<EntityServiceSet>) {
        serviceAdapter = ServiceAdapter(finalEntityServices)
        binding.layoutStep4.rvServices.adapter = serviceAdapter
        binding.layoutStep4.rvServices.setItemViewCacheSize(1000)
        serviceAdapter.submitList(selectedServices)
        serviceAdapter.onServiceSelected = { entityService, position ->
            selectedServices[position] = selectedServices[position].copy(
                entityServiceId = entityService.entityServiceId,
                name = entityService.name
            )
        }
        serviceAdapter.onServiceDescType = { description, position ->
            selectedServices[position] = selectedServices[position].copy(
                description = description
            )
        }

        binding.layoutStep4.btnAddService.setOnClickListener {
            selectedServices.add(EntityServiceSet(name = "Select Service"))
            serviceAdapter.submitList(selectedServices)
            serviceAdapter.notifyItemRangeInserted(selectedServices.lastIndex, selectedServices.size)
        }
    }

    private fun accoladesObserver() {
        addEntityViewModel.accolades.observe(viewLifecycleOwner) {
            it.accolades?.let { accolades ->
                val finalAccolades = accolades.toMutableList()
                finalAccolades.add(0, EntityAccoladeSet(name = "Select Accolade"))
                setupAccoladesAdapter(finalAccolades)
            }
        }
    }

    private fun setupAccoladesAdapter(finalAccolades: MutableList<EntityAccoladeSet>) {
        accoladeAdapter = AccoladeAdapter(finalAccolades)
        binding.layoutStep4.rvAccolades.adapter = accoladeAdapter
        binding.layoutStep4.rvAccolades.setItemViewCacheSize(1000)
        accoladeAdapter.submitList(selectedAccolades)
        accoladeAdapter.onAccoladeSelected = { entityAccolade, position ->
            selectedAccolades[position] = selectedAccolades[position].copy(
                entityAccoladeId = entityAccolade.entityAccoladeId,
                name = entityAccolade.name
            )
        }
        accoladeAdapter.onAccoladeDescType = { description, position ->
            selectedAccolades[position] = selectedAccolades[position].copy(
                note = description
            )
        }

        binding.layoutStep4.btnAddAccolade.setOnClickListener {
            selectedAccolades.add(EntityAccoladeSet(name = "Select Accolade"))
            accoladeAdapter.submitList(selectedAccolades)
            accoladeAdapter.notifyItemRangeInserted(selectedAccolades.lastIndex, selectedAccolades.size)
        }
    }

    // Other

    private fun populateYears() {
        val years = getYearsTillDate()
        val yearsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)
        binding.layoutStep1.spYear.setAdapter(yearsAdapter)
        binding.layoutStep1.spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRegYear = if (position > 0) yearsAdapter.getItem(position)?.toInt() else null
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun populateMonths() {
        val months = getMonths()
        val monthsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        binding.layoutStep1.spMonth.setAdapter(monthsAdapter)
        binding.layoutStep1.spMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRegMonth = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getYearsTillDate(): List<String> {
        val years = mutableListOf("Select Year")
        val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1950 .. currentYear) {
            years.add(i.toString())
        }
        return years
    }

    private fun getMonths(): List<String> {
        return listOf("Select Month", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Aug", "Oct", "Nov", "Dec")
    }

    // Step Observer
    private fun stepNumberObserver() {
        binding.btnNext.setOnClickListener {
            val currentValue = addEntityViewModel.stepNumber.value!!
            if (currentValue == 1 && isForm1Valid()) {
                addEntityViewModel.stepNumber.postValue(2)
            }
            if (currentValue == 2 && isForm2Valid()) {
                addEntityViewModel.stepNumber.postValue(3)
            }
            if (currentValue == 3 && isForm3Valid()) {
                addEntityViewModel.stepNumber.postValue(4)
            }
            if (currentValue == 4) {
                addEntity()
            }
        }
        binding.btnPrev.setOnClickListener {
            val currentValue = addEntityViewModel.stepNumber.value!!
            when (currentValue) {
                2 -> addEntityViewModel.stepNumber.postValue(1)
                3 -> addEntityViewModel.stepNumber.postValue(2)
                4 -> addEntityViewModel.stepNumber.postValue(3)
            }
        }
        addEntityViewModel.stepNumber.observe(viewLifecycleOwner) {
            binding.btnPrev.isVisible = it != 1
            binding.btnNext.isVisible = it in 1..4
            binding.btnNext.text = if (it in 1..3) "Next" else if (it == 4) { if (args.entityId == null) "Submit" else "Update" } else "Invalid State"

            binding.layoutStep1.root.isVisible = it == 1
            binding.layoutStep2.root.isVisible = it == 2
            binding.layoutStep3.root.isVisible = it == 3
            binding.layoutStep4.root.isVisible = it == 4
        }
    }

    private fun isForm1Valid(): Boolean {
        if (binding.layoutStep1.spEntityType.selectedItemPosition == 0) {
            binding.root.snack("Select Entity Type")
            return false
        }
        if (binding.layoutStep1.etEntityName.text.toString().isEmpty()) {
            binding.root.snack("Enter Entity Name")
            binding.layoutStep1.etEntityName.error = "Required"
            binding.layoutStep1.etEntityName.requestFocus()
            return false
        }
        return true
    }

    private fun isForm2Valid(): Boolean {
        if (binding.layoutStep2.etAddressLine1.text.toString().isEmpty()) {
            binding.root.snack("Enter Address Line 1")
            binding.layoutStep2.etAddressLine1.error = "Required"
            binding.layoutStep2.etAddressLine1.requestFocus()
            return false
        }
        if (binding.layoutStep2.etPincode.text.toString().isEmpty()) {
            binding.root.snack("Enter Pincode")
            binding.layoutStep2.etPincode.error = "Required"
            binding.layoutStep2.etPincode.requestFocus()
            return false
        }
        if (binding.layoutStep2.etState.text.toString().isEmpty()) {
            binding.root.snack("Enter State")
            binding.layoutStep2.etState.error = "Required"
            binding.layoutStep2.etState.requestFocus()
            return false
        }
        if (binding.layoutStep2.etCity.text.toString().isEmpty()) {
            binding.root.snack("Enter City")
            binding.layoutStep2.etCity.error = "Required"
            binding.layoutStep2.etCity.requestFocus()
            return false
        }
        if (binding.layoutStep2.etArea.text.toString().isEmpty()) {
            binding.root.snack("Enter Area")
            binding.layoutStep2.etArea.error = "Required"
            binding.layoutStep2.etArea.requestFocus()
            return false
        }
        if (binding.layoutStep2.etLandmark.text.toString().isEmpty()) {
            binding.root.snack("Enter Landmark")
            binding.layoutStep2.etLandmark.error = "Required"
            binding.layoutStep2.etLandmark.requestFocus()
            return false
        }
        return true
    }

    private fun isForm3Valid(): Boolean {
        val validMobileNos = selectedMobileNumbers.filter { it1 -> it1.toString().isNotEmpty() && it1.toString().length == 10 }
        val validLandLineNos = selectedLandlineNumbers.filter { it1 -> !it1.stdCode.isNullOrEmpty() || it1.landlineNumber != null && it1.landlineNumber != 0L }
        if (validMobileNos.isEmpty() && validLandLineNos.isEmpty()) {
            binding.root.snack("Enter One Mob No or Landline Number")
            return false
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                goBack()
                true
            }
            else -> false
        }
    }

    private fun goBack() {
        val currentStep = addEntityViewModel.stepNumber.value!!
        if (currentStep > 1)
            addEntityViewModel.stepNumber.postValue(currentStep-1)
        else
            navController?.popBackStack()
    }
}