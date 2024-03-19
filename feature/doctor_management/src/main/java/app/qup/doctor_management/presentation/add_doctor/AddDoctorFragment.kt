package app.qup.doctor_management.presentation.add_doctor

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.databinding.FragmentAddDoctorBinding
import app.qup.doctor_management.domain.model.Doctor
import app.qup.doctor_management.domain.model.Speciality
import app.qup.doctor_management.domain.model.SpecialityCategory
import app.qup.doctor_management.domain.model.User
import app.qup.doctor_management.domain.model.toQualificationDegreeSet
import app.qup.doctor_management.domain.model.toSpecialitySet
import app.qup.ui.common.snack
import app.qup.util.common.DEFAULT_LANGUAGE_ID
import app.qup.util.common.LOCAL_DATE_FORMAT
import app.qup.util.common.millisToDateString
import app.qup.util.common.transformIntoDatePicker
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddDoctorFragment : Fragment(), MenuProvider {
    private var _binding: FragmentAddDoctorBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    private val args by navArgs<AddDoctorFragmentArgs>()
    private val addDoctorViewModel by viewModels<AddDoctorViewModel>()

    private var selectedGender : String? = null
    private var selectedBloodGroup : String? = null
    private var selectedBloodType : String? = null

    private val mSDF = SimpleDateFormat(LOCAL_DATE_FORMAT, Locale.getDefault())

    private lateinit var otherDegreesAdapter: DegreesAdapter

    private lateinit var otherSpecialityAdapter: SpecialityAdapter
    private lateinit var accoladesAdapter: AccoladesAdapter
    private lateinit var medAchievementAdapter: MedAchievementAdapter
    private lateinit var nonMedAchievementAdapter: NonMedAchievementAdapter

    private var selectedRegYear: Int? = null
    private var selectedRegMonth: Int? = null
    private var primaryDegree: QualificationDegreeSet? = null
    private var primarySpecialityCategory: SpecialityCategory? = null
    private var primarySpeciality: SpecialitySet? = null

    private var allDegrees = mutableListOf<QualificationDegreeSet>()
    private var allAccolades = mutableListOf<AccoladesSet>()
    private var allSpecialityCategories = mutableListOf<SpecialityCategory>()
    private var allSpecialities = mutableListOf<SpecialitySet>()

    private val selectedOtherDegrees = mutableListOf(QualificationDegreeSet(name = "Select Degree"))
    private val selectedOtherSpecialities = mutableListOf(SpecialitySet(name = "Select Speciality"))
    private val selectedAccolades = mutableListOf(AccoladesSet(accoladeType =  "Select Accolade"))
    private val selectedMedAchievements = mutableListOf("")
    private val selectedNonMedAchievements = mutableListOf("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDoctorBinding.inflate(inflater, container, false)
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
        // Step 1
        binding.layoutStep1.etDob.transformIntoDatePicker(requireContext(), LOCAL_DATE_FORMAT)
        binding.layoutStep1.etMobileNumber.isEnabled = args.mobileNumber == null
        args.mobileNumber?.let {
            binding.layoutStep1.etMobileNumber.setText(args.mobileNumber)
        }

        loadGenders()
        bloodGroupLister()
        etMobileNumberListener()
        // Step 2
        populateYears()
        populateMonths()
        populateDegrees()
        addOtherDegree()
        // Step 3
        populateSpecialityCategory()
        addOtherSpeciality()
        // Step 4
        setupMedAchievementAdapter()
        setupNonMedAchievementAdapter()
        addAccolades()
        accoladesObserver()
        addMedAchievement()
        addNonMedAchievement()

        args.doctorId?.let {
            loadDoctorData()
        } ?: run {
            getFormData()
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun getFormData() {
        addDoctorViewModel.getDegrees()
        addDoctorViewModel.getAccolades()
        addDoctorViewModel.getSpecialityCategories()
    }

    private fun loadDoctorData() {
        addDoctorViewModel.getUserByNumber(args.mobileNumber!!)
        addDoctorViewModel.userInfo.observe(viewLifecycleOwner) {
            it.user?.let { user ->
                fillUserData(user)
                addDoctorViewModel.getDoctorById(args.doctorId!!)
            }
        }
        addDoctorViewModel.getDoctorById.observe(viewLifecycleOwner) {
            it.doctor?.let { doctor ->
                fillDoctorData(doctor)
                getFormData()
            }
        }
    }

    private fun fillUserData(user: User) {
        binding.layoutStep1.etFirstName.setText(user.firstName)
        binding.layoutStep1.etLastName.setText(user.lastName)

        val allRadioButtons = mutableListOf<RadioButton>()
        for (i in 0 until binding.layoutStep1.rgGender.childCount) {
            allRadioButtons.add(binding.layoutStep1.rgGender.getChildAt(i) as RadioButton)
        }

        allRadioButtons.forEach { radioButton ->
            radioButton.isChecked = radioButton.text == user.gender
        }

        binding.layoutStep1.etDob.setText(millisToDateString(user.dateOfBirth))
        binding.layoutStep1.etEmail.setText(user.emailId)

        val bloodGroup = user.bloodGroup
        val group = bloodGroup.replace("+", "").replace("-", "")
        if (bloodGroup.isNotEmpty())
            binding.layoutStep1.tgBloodGroup.check(
                when (group) {
                    "A" -> binding.layoutStep1.optionA.id
                    "B" -> binding.layoutStep1.optionB.id
                    "AB" -> binding.layoutStep1.optionAb.id
                    "O" -> binding.layoutStep1.optionO.id
                    else -> -1
                }
            )
        if (bloodGroup.contains("+")) {
            binding.layoutStep1.tgBloodType.check(binding.layoutStep1.optionPositive.id)
        } else if (bloodGroup.contains("-")) {
            binding.layoutStep1.tgBloodType.check(binding.layoutStep1.optionNegative.id)
        } else {
            binding.layoutStep1.tgBloodType.check(-1)
        }
        getFormData()
    }

    private fun fillDoctorData(doctor: Doctor) {
        // Step 2
        binding.layoutStep2.etRegistrationNumber.setText(doctor.registrationNumber)
        binding.layoutStep2.spYear.setSelection(getYearsTillDate().indexOf(doctor.registrationYear.toString()), true)
        binding.layoutStep2.spMonth.setSelection(doctor.registrationMonth, true)
        // Degree Fill
        val primaryDegreeLocal = doctor.qualificationDegreeSet.filter { it1 -> it1.primary == true }
        val nonPrimaryDegreeLocal = doctor.qualificationDegreeSet.filter { it1 -> it1.primary == false }
        if (primaryDegreeLocal.isNotEmpty()) {
            primaryDegree = primaryDegreeLocal[0]
        }
        if (nonPrimaryDegreeLocal.isNotEmpty()) selectedOtherDegrees.clear()
        selectedOtherDegrees.addAll(nonPrimaryDegreeLocal)
        // Step 3
        val primarySpecialityLocal = doctor.specialitySet.filter { it1 -> it1.primary == true }
        val nonPrimarySpecialityLocal = doctor.specialitySet.filter { it1 -> it1.primary == false }
        if (primarySpecialityLocal.isNotEmpty()) {
            primarySpeciality = primarySpecialityLocal[0]
        }
        if (nonPrimarySpecialityLocal.isNotEmpty()) selectedOtherSpecialities.clear()
        selectedOtherSpecialities.addAll(nonPrimarySpecialityLocal)
        // Step 4
        if (doctor.accoladesSet.isNotEmpty()) selectedAccolades.clear()
        selectedAccolades.addAll(doctor.accoladesSet)
        if (doctor.medicalAchievements.isNotEmpty()) selectedMedAchievements.clear()
        selectedMedAchievements.addAll(doctor.medicalAchievements)
        if (doctor.nonMedicalAchievements.isNotEmpty()) selectedNonMedAchievements.clear()
        selectedNonMedAchievements.addAll(doctor.nonMedicalAchievements)
    }

    @SuppressLint("SetTextI18n")
    private fun etMobileNumberListener() {
        binding.layoutStep1.etMobileNumber.addTextChangedListener {
            val mobileNumber = it.toString()
            if (mobileNumber.length == 10) {
                addDoctorViewModel.searchDoctorByName(mobileNumber = mobileNumber)
            } else {
                binding.layoutStep1.lblError.isVisible = false
            }
        }
        addDoctorViewModel.searchDoctorByName.observe(viewLifecycleOwner) {
            it.doctorR?.let {
                binding.layoutStep1.lblError.isVisible = true
                binding.layoutStep1.lblError.text = "Doctor Already Exist With This Number"
            }
        }
    }

    // Add Doctor
    private fun addUpdateDoctor() {
        // Step 1
        val mobileNumber = binding.layoutStep1.etMobileNumber.text.toString()
        val firstName = binding.layoutStep1.etFirstName.text.toString()
        val lastName = binding.layoutStep1.etLastName.text.toString()
        val emailId = binding.layoutStep1.etEmail.text.toString()
        val dobText = binding.layoutStep1.etDob.text.toString()

        // Step 2
        val registrationNumber = binding.layoutStep2.etRegistrationNumber.text.toString()

        val finalQualificationDegreeSet = primaryDegree?.let { mutableListOf(it) }
        finalQualificationDegreeSet?.addAll(selectedOtherDegrees)

        val finalSpecialities = primarySpeciality?.let { mutableListOf(it) }
        finalSpecialities?.addAll(selectedOtherSpecialities)

        val doctorRequestDto = DoctorRequestDto(
            // Step 1
            mobileNumber = mobileNumber.toLong(),
            firstName = firstName,
            lastName = lastName,
            gender = selectedGender,
            dateOfBirth = if (dobText.isEmpty()) null else DateTime(mSDF.parse(dobText), DateTimeZone.UTC).toString(),
            emailId = emailId,
            bloodGroup = "$selectedBloodGroup$selectedBloodType",

            // Step 2
            registrationNumber = registrationNumber,
            registrationYear = selectedRegYear,
            registrationMonth = selectedRegMonth,
            qualificationDegreeSet = finalQualificationDegreeSet?.filter { it1 -> it1.name != "Select Degree"},

            // Step 3
            specialitySet = finalSpecialities?.filter { it1 -> it1.name != "Select Speciality" },

            // Step 4
            accoladesSet = selectedAccolades.filter { it1 -> it1.accoladeType != "Select Accolade" },
            medicalAchievements = selectedMedAchievements.filter { it1 -> it1.isNotEmpty() },
            nonMedicalAchievements = selectedNonMedAchievements.filter { it1 -> it1.isNotEmpty() },

            // Other
            preferredLanguageId = DEFAULT_LANGUAGE_ID,
            communicationType = mutableListOf(),
        )
        args.doctorId?.let {
            addDoctorViewModel.updateDoctor(
                id = args.doctorId!!,
                doctorRequestDto = doctorRequestDto
            )
        } ?: run {
            addDoctorViewModel.addDoctor(
                doctorRequestDto = doctorRequestDto
            )
        }
        addDoctorViewModel.addDoctor.observe(viewLifecycleOwner) {
            it.doctor?.let {
                navController?.popBackStack()
            }
            binding.progressBar.isVisible = it.isLoading
            binding.btnNext.isEnabled = !it.isLoading
        }
        addDoctorViewModel.updateDoctor.observe(viewLifecycleOwner) {
            it.doctor?.let {
                navController?.popBackStack()
            }
            binding.progressBar.isVisible = it.isLoading
            binding.btnNext.isEnabled = !it.isLoading
        }
    }

    // Step 1
    private fun loadGenders() {
        addDoctorViewModel.genders.observe(viewLifecycleOwner) {
            it.genders?.let {  genders ->
                genders.forEach { gender ->
                    val rb = RadioButton(requireContext()).apply {
                        text = gender
                    }
                    binding.layoutStep1.rgGender.addView(rb)
                }
            }
        }

        binding.layoutStep1.rgGender.setOnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById<RadioButton>(checkedId)
            if (rb != null) {
                selectedGender = rb.text.toString()
            }
        }
    }

    private fun bloodGroupLister() {
        binding.layoutStep1.tgBloodGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val tb = group.findViewById<MaterialButton>(checkedId)
                selectedBloodGroup = tb.text.toString()
            }
        }
        binding.layoutStep1.tgBloodType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val tb = group.findViewById<MaterialButton>(checkedId)
                selectedBloodType = tb.text.toString()
            }
        }
    }

    // Step 2
    private fun populateYears() {
        val years = getYearsTillDate()
        val yearsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)
        binding.layoutStep2.spYear.setAdapter(yearsAdapter)
        binding.layoutStep2.spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRegYear = if (position > 0) yearsAdapter.getItem(position)?.toInt() else null
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun populateMonths() {
        val months = getMonths()
        val monthsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        binding.layoutStep2.spMonth.setAdapter(monthsAdapter)
        binding.layoutStep2.spMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRegMonth = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun populateDegrees() {
        addDoctorViewModel.degrees.observe(viewLifecycleOwner) {
            it.degrees?.let { degrees ->
                val finalDegrees: MutableList<QualificationDegreeSet> = degrees.map { it1 -> it1.toQualificationDegreeSet() }.toMutableList()
                finalDegrees.add(0, QualificationDegreeSet(name = "Select Degree"))
                // Init All Degrees
                allDegrees = finalDegrees
                val degreesAdapter: ArrayAdapter<QualificationDegreeSet?> =
                    object : ArrayAdapter<QualificationDegreeSet?>(requireContext(), app.qup.ui.R.layout.list_item_array_adapter, finalDegrees.toList()) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.text = getItem(position)?.name
                            return view
                        }

                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.setPadding(16,16,16,16)
                            view.text = getItem(position)?.name
                            return view
                        }
                    }
                binding.layoutStep2.layoutPrimaryDegree.spSelectDegree.setAdapter(degreesAdapter)
                binding.layoutStep2.layoutPrimaryDegree.spSelectDegree.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        primaryDegree = degreesAdapter.getItem(position) as QualificationDegreeSet
                        primaryDegree = primaryDegree?.copy(primary = true)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                // Setup Degrees Adapter
                otherDegreesAdapter = DegreesAdapter(allDegrees)
                binding.layoutStep2.rvOtherDegrees.adapter = otherDegreesAdapter
                binding.layoutStep2.rvOtherDegrees.setItemViewCacheSize(1000)
                otherDegreesAdapter.submitList(selectedOtherDegrees)
                otherDegreesAdapter.onDegreeSelected = { selectedDegree, position ->
                    selectedOtherDegrees[position] = selectedOtherDegrees[position].copy(
                        name = selectedDegree.name,
                        educationDegreeId = selectedDegree.educationDegreeId,
                        primary = false
                    )
                }
                otherDegreesAdapter.onLocationType = { location, position ->
                    selectedOtherDegrees[position] = selectedOtherDegrees[position].copy(
                        location = location
                    )
                }

                primaryDegree?.let {
                    binding.layoutStep2.layoutPrimaryDegree.spSelectDegree.setSelection(allDegrees.map { it1 -> it1.educationDegreeId }.indexOf(primaryDegree!!.educationDegreeId), true)
                    binding.layoutStep2.layoutPrimaryDegree.etAddLocation.setText(primaryDegree!!.location)
                }
            }
        }
        binding.layoutStep2.layoutPrimaryDegree.etAddLocation.addTextChangedListener {
            val input = it.toString()
            primaryDegree = primaryDegree?.copy(
                location = input
            )
        }
    }

    private fun addOtherDegree() {
        binding.layoutStep2.btnAddDegree.setOnClickListener {
            selectedOtherDegrees.add(QualificationDegreeSet(name = "Select Degree"))
            otherDegreesAdapter.submitList(selectedOtherDegrees)
            otherDegreesAdapter.notifyItemRangeInserted(selectedOtherDegrees.lastIndex, selectedOtherDegrees.size)
        }
    }

    // Step 3

    private fun populateSpecialityCategory() {
        addDoctorViewModel.specialityCategories.observe(viewLifecycleOwner) {
            it.specialityCategories?.let { specialityCategories ->
                val finalSpecialityCategories: MutableList<SpecialityCategory> = specialityCategories.toMutableList()
                finalSpecialityCategories.add(0, SpecialityCategory(name = "Select Category"))
                allSpecialityCategories = finalSpecialityCategories
                populateSpeciality(allSpecialityCategories)
                val specialityCategoryAdapter: ArrayAdapter<SpecialityCategory?> =
                    object : ArrayAdapter<SpecialityCategory?>(requireContext(), app.qup.ui.R.layout.list_item_array_adapter, finalSpecialityCategories.toList()) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.text = getItem(position)?.name
                            return view
                        }

                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.setPadding(16,16,16,16)
                            view.text = getItem(position)?.name
                            return view
                        }
                    }
                binding.layoutStep3.layoutPrimarySpeciality.spSelectCategory.setAdapter(specialityCategoryAdapter)
                binding.layoutStep3.layoutPrimarySpeciality.spSelectCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        primarySpecialityCategory = specialityCategoryAdapter.getItem(position) as SpecialityCategory
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
                addDoctorViewModel.getSpecialities()
            }
        }
    }

    private fun populateSpeciality(allSpecialityCategories: MutableList<SpecialityCategory>) {
        addDoctorViewModel.specialities.observe(viewLifecycleOwner) {
            it.specialities?.let { specialities ->
                val finalSpecialities: MutableList<SpecialitySet> = specialities.map { it1 -> it1.toSpecialitySet() }.toMutableList()
                finalSpecialities.add(0, SpecialitySet(name = "Select Speciality"))
                allSpecialities = finalSpecialities
                val specialityAdapter: ArrayAdapter<SpecialitySet?> =
                    object : ArrayAdapter<SpecialitySet?>(requireContext(), app.qup.ui.R.layout.list_item_array_adapter, finalSpecialities.toList()) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.text = getItem(position)?.name
                            return view
                        }

                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.setPadding(16,16,16,16)
                            view.text = getItem(position)?.name
                            return view
                        }
                    }
                binding.layoutStep3.layoutPrimarySpeciality.spSelectSpeciality.setAdapter(specialityAdapter)
                binding.layoutStep3.layoutPrimarySpeciality.spSelectSpeciality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        primarySpeciality = specialityAdapter.getItem(position) as SpecialitySet
                        primarySpeciality = primarySpeciality?.copy(primary = true)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
                setupOtherSpecialityAdapter(allSpecialityCategories, allSpecialities, specialities)

                primarySpeciality?.let {
                    val selectSpecialityIndex = allSpecialities.map { it1 -> it1.specialityId }.indexOf(primarySpeciality!!.specialityId)
                    binding.layoutStep3.layoutPrimarySpeciality.spSelectSpeciality.setSelection(selectSpecialityIndex)

                    val findCategoryId = specialities[selectSpecialityIndex].specialityCategory.specialityCategoryId
                    if (!findCategoryId.isNullOrEmpty()) {
                        binding.layoutStep3.layoutPrimarySpeciality.spSelectCategory.setSelection(allSpecialityCategories.map { it1 -> it1.specialityCategoryId }.indexOf(findCategoryId))
                    }
                }

            }
        }
    }

    private fun setupOtherSpecialityAdapter(
        allSpecialityCategories: MutableList<SpecialityCategory>,
        allSpecialities: MutableList<SpecialitySet>,
        specialities: List<Speciality>
    ) {
        otherSpecialityAdapter = SpecialityAdapter(allSpecialityCategories, allSpecialities, specialities)
        binding.layoutStep3.rvOtherSpecialities.adapter = otherSpecialityAdapter
        otherSpecialityAdapter.submitList(selectedOtherSpecialities)
        otherSpecialityAdapter.selectedSpeciality = { selectedSpeciality, position ->
            selectedOtherSpecialities[position] = selectedOtherSpecialities[position].copy(
                name = selectedSpeciality.name,
                primary = false,
                specialityId = selectedSpeciality.specialityId,
            )
        }
    }

    private fun addOtherSpeciality() {
        binding.layoutStep3.btnAddSpeciality.setOnClickListener {
            selectedOtherSpecialities.add(SpecialitySet(name = "Select Speciality"))
            otherSpecialityAdapter.submitList(selectedOtherSpecialities)
            otherSpecialityAdapter.notifyItemRangeInserted(selectedOtherSpecialities.lastIndex, selectedOtherSpecialities.size)
        }
    }

    // Step 4
    private fun setupMedAchievementAdapter() {
        medAchievementAdapter = MedAchievementAdapter()
        binding.layoutStep4.rvMedicalAchievements.adapter = medAchievementAdapter
        binding.layoutStep4.rvMedicalAchievements.setItemViewCacheSize(1000)
        medAchievementAdapter.submitList(selectedMedAchievements)
        medAchievementAdapter.onMedAchievementType = { achievement, position ->
            Log.d("TAG", "setupMedAchievementAdapter: before $achievement")
            selectedMedAchievements[position] = achievement
            Log.d("TAG", "setupMedAchievementAdapter: after ${selectedMedAchievements[position]}")
        }
    }

    private fun setupNonMedAchievementAdapter() {
        nonMedAchievementAdapter = NonMedAchievementAdapter()
        binding.layoutStep4.rvNonMedicalAchievements.adapter = nonMedAchievementAdapter
        binding.layoutStep4.rvNonMedicalAchievements.setItemViewCacheSize(1000)
        nonMedAchievementAdapter.submitList(selectedNonMedAchievements)
        nonMedAchievementAdapter.onNonMedAchievementType = { achievement, position ->
            selectedNonMedAchievements[position] = achievement
        }
    }

    private fun accoladesObserver() {
        addDoctorViewModel.accolades.observe(viewLifecycleOwner) {
            it.accolades?.let { accolades ->
                val finalAccolades = accolades.toMutableList()
                finalAccolades.add(0, AccoladesSet(accoladeType =  "Select Accolade"))
                allAccolades = finalAccolades
                setupAccoladesAdapter(allAccolades)
            }
        }
    }

    private fun addAccolades() {
        binding.layoutStep4.btnAddAccolades.setOnClickListener {
            selectedAccolades.add(AccoladesSet(accoladeType = "Select Accolade"))
            accoladesAdapter.submitList(selectedAccolades)
            accoladesAdapter.notifyItemRangeInserted(selectedAccolades.lastIndex, selectedAccolades.size)
        }
    }

    private fun setupAccoladesAdapter(accoladesSets: List<AccoladesSet>) {
        accoladesAdapter = AccoladesAdapter(accoladesSets)
        binding.layoutStep4.rvAccolades.adapter = accoladesAdapter
        binding.layoutStep4.rvAccolades.setItemViewCacheSize(1000)
        accoladesAdapter.submitList(selectedAccolades)
        accoladesAdapter.selectedAccolade = { accoladesSet, position ->
            selectedAccolades[position] = selectedAccolades[position].copy(
                accoladeType = accoladesSet.accoladeType,
                accoladeTypeId = accoladesSet.accoladeTypeId,
                iconName = accoladesSet.iconName
            )
        }
        accoladesAdapter.onNoteType = { note, position ->
            selectedAccolades[position] = selectedAccolades[position].copy(
                notes = note
            )
        }
    }

    private fun addMedAchievement() {
        binding.layoutStep4.btnAddMedicalAchievements.setOnClickListener {
            selectedMedAchievements.add("")
            medAchievementAdapter.submitList(selectedMedAchievements)
            medAchievementAdapter.notifyItemRangeInserted(selectedMedAchievements.lastIndex, selectedMedAchievements.size)
        }
    }

    private fun addNonMedAchievement() {
        binding.layoutStep4.btnAddNonMedicalAchievements.setOnClickListener {
            selectedNonMedAchievements.add("")
            nonMedAchievementAdapter.submitList(selectedNonMedAchievements)
            nonMedAchievementAdapter.notifyItemRangeInserted(selectedNonMedAchievements.lastIndex, selectedNonMedAchievements.size)
        }
    }

    private fun stepNumberObserver() {
        binding.btnNext.setOnClickListener {
            val currentValue = addDoctorViewModel.stepNumber.value!!
            if (currentValue == 1 && isForm1Valid()) {
                addDoctorViewModel.stepNumber.postValue(2)
            }
            if (currentValue == 2 && isForm2Valid()) {
                addDoctorViewModel.stepNumber.postValue(3)
            }
            if (currentValue == 3 && isForm3Valid()) {
                addDoctorViewModel.stepNumber.postValue(4)
            }
            if (currentValue == 4) {
                addUpdateDoctor()
            }
        }
        binding.btnPrev.setOnClickListener {
            val currentValue = addDoctorViewModel.stepNumber.value!!
            when (currentValue) {
                2 -> addDoctorViewModel.stepNumber.postValue(1)
                3 -> addDoctorViewModel.stepNumber.postValue(2)
                4 -> addDoctorViewModel.stepNumber.postValue(3)
            }
        }
        addDoctorViewModel.stepNumber.observe(viewLifecycleOwner) {
            binding.btnPrev.isVisible = it != 1
            binding.btnNext.isVisible = it in 1..4
            binding.btnNext.text = if (it in 1..3) "Next" else if (it == 4) { if (args.doctorId == null) "Submit" else "Update" } else "Invalid State"

            binding.layoutStep1.root.isVisible = it == 1
            binding.layoutStep2.root.isVisible = it == 2
            binding.layoutStep3.root.isVisible = it == 3
            binding.layoutStep4.root.isVisible = it == 4
        }
    }

    private fun isForm1Valid(): Boolean {
        if (binding.layoutStep1.etMobileNumber.text.toString().length != 10) {
            binding.root.snack("Enter Valid Mobile No.")
            binding.layoutStep1.etMobileNumber.error = "Required"
            binding.layoutStep1.etMobileNumber.requestFocus()
            return false
        }
        if (binding.layoutStep1.etFirstName.text.isEmpty()) {
            binding.root.snack("Enter First Name")
            binding.layoutStep1.etFirstName.error = "Required"
            binding.layoutStep1.etFirstName.requestFocus()
            return false
        }
        if (binding.layoutStep1.etLastName.text.isEmpty()) {
            binding.root.snack("Enter Last Name")
            binding.layoutStep1.etLastName.error = "Required"
            binding.layoutStep1.etLastName.requestFocus()
            return false
        }
        if (selectedGender == null) {
            binding.root.snack("Select Gender")
            return false
        }
        if (binding.layoutStep1.etEmail.text.toString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(binding.layoutStep1.etEmail.text.toString()).matches()) {
            binding.root.snack("Enter Valid Email")
            return false
        }
        if (selectedBloodGroup != null && selectedBloodType == null) {
            binding.root.snack("Select Blood Type")
            return false
        }
        if (selectedBloodGroup == null && selectedBloodType != null) {
            binding.root.snack("Select Blood Group")
            return false
        }
        return true
    }

    private fun isForm2Valid(): Boolean {
        if (binding.layoutStep2.etRegistrationNumber.text.isEmpty()) {
            binding.root.snack("Enter Registration Number")
            binding.layoutStep2.etRegistrationNumber.error = "Required"
            binding.layoutStep2.etRegistrationNumber.requestFocus()
            return false
        }
        if (binding.layoutStep2.spYear.selectedItemPosition == 0) {
            binding.root.snack("Select Year")
            return false
        }
        if (binding.layoutStep2.spMonth.selectedItemPosition == 0) {
            binding.root.snack("Select Month")
            return false
        }
        if (binding.layoutStep2.layoutPrimaryDegree.spSelectDegree.selectedItemPosition == 0) {
            binding.root.snack("Select QualificationDegreeSet")
            return false
        }
        return true
    }

    private fun isForm3Valid(): Boolean {
        if (binding.layoutStep3.layoutPrimarySpeciality.spSelectCategory.selectedItemPosition == 0) {
            binding.root.snack("Select Category")
            return false
        }
        if (binding.layoutStep3.layoutPrimarySpeciality.spSelectSpeciality.selectedItemPosition == 0) {
            binding.root.snack("Select Speciality")
            return false
        }
        return true
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
        val currentStep = addDoctorViewModel.stepNumber.value!!
        if (currentStep > 1)
            addDoctorViewModel.stepNumber.postValue(currentStep-1)
        else
            navController?.popBackStack()
    }
}