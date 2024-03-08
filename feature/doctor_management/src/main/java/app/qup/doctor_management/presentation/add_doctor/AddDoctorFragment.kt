package app.qup.doctor_management.presentation.add_doctor

import android.os.Bundle
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
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.databinding.FragmentAddDoctorBinding
import app.qup.doctor_management.domain.model.SpecialityCategory
import app.qup.doctor_management.domain.model.toQualificationDegreeSet
import app.qup.doctor_management.domain.model.toSpecialitySet
import app.qup.ui.common.snack
import app.qup.util.common.DEFAULT_LANGUAGE_ID
import app.qup.util.common.LOCAL_DATE_FORMAT
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

    private val addDoctorViewModel by viewModels<AddDoctorViewModel>()

    private var selectedGender : String? = null
    private var selectedBloodGroup : String? = null
    private var selectedBloodType : String? = null

    private val mSDF = SimpleDateFormat(LOCAL_DATE_FORMAT, Locale.getDefault())

    private lateinit var otherDegreesAdapter: DegreesAdapter

    private lateinit var medAchievementAdapter: MedAchievementAdapter
    private lateinit var nonMedAchievementAdapter: NonMedAchievementAdapter

    private var selectedRegYear: Int? = null
    private var selectedRegMonth: Int? = null
    private var primaryDegree: QualificationDegreeSet? = null
    private var primarySpecialityCategory: SpecialityCategory? = null
    private var primarySpeciality: SpecialitySet? = null

    private var allDegrees = mutableListOf<QualificationDegreeSet>()
    private val selectedOtherDegrees = mutableListOf(QualificationDegreeSet(name = "Select Degree"))
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
        loadGenders()
        bloodGroupLister()
        // Step 2
        populateYears()
        populateMonths()
        populateDegrees()
        addOtherDegree()
        // Step 3
        populateSpecialityCategory()
        populateSpeciality()
        // Step 4
        setupMedAchievementAdapter()
        setupNonMedAchievementAdapter()
        addMedAchievement()
        addNonMedAchievement()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    // Add Doctor
    private fun addDoctor() {
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

        addDoctorViewModel.addDoctor(
            doctorRequestDto = DoctorRequestDto(
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
                specialitySet = primarySpeciality?.let { mutableListOf(it) },

                // Step 4
                accoladesSet = mutableListOf(),
                medicalAchievements = selectedNonMedAchievements.filter { it1 -> it1.isNotEmpty() },
                nonMedicalAchievements = selectedNonMedAchievements.filter { it1 -> it1.isNotEmpty() },

                // Other
                preferredLanguageId = DEFAULT_LANGUAGE_ID,
                communicationType = mutableListOf(),
            )
        )
        addDoctorViewModel.addDoctor.observe(viewLifecycleOwner) {
            it.doctor?.let {
                navController?.popBackStack()
            }
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
                val degreesAdapter: ArrayAdapter<SpecialityCategory?> =
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
                binding.layoutStep3.layoutPrimarySpeciality.spSelectCategory.setAdapter(degreesAdapter)
                binding.layoutStep3.layoutPrimarySpeciality.spSelectCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        primarySpecialityCategory = degreesAdapter.getItem(position) as SpecialityCategory
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }

    private fun populateSpeciality() {
        addDoctorViewModel.specialities.observe(viewLifecycleOwner) {
            it.specialities?.let { specialities ->
                val finalSpecialities: MutableList<SpecialitySet> = specialities.map { it1 -> it1.toSpecialitySet() }.toMutableList()
                finalSpecialities.add(0, SpecialitySet(name = "Select SpecialitySet"))
                val degreesAdapter: ArrayAdapter<SpecialitySet?> =
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
                binding.layoutStep3.layoutPrimarySpeciality.spSelectSpeciality.setAdapter(degreesAdapter)
                binding.layoutStep3.layoutPrimarySpeciality.spSelectSpeciality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        primarySpeciality = degreesAdapter.getItem(position) as SpecialitySet
                        primarySpeciality = primarySpeciality?.copy(primary = true)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }

    // Step 4
    private fun setupMedAchievementAdapter() {
        medAchievementAdapter = MedAchievementAdapter()
        binding.layoutStep4.rvMedicalAchievements.adapter = medAchievementAdapter
        binding.layoutStep4.rvMedicalAchievements.setItemViewCacheSize(1000)
        medAchievementAdapter.submitList(selectedMedAchievements)
        medAchievementAdapter.onMedAchievementType = { achievement, position ->
            selectedMedAchievements[position] = achievement
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
                addDoctor()
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
            binding.btnNext.text = if (it in 1..3) "Next" else if (it == 4) "Submit" else "Invalid State"

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
            binding.root.snack("Select SpecialitySet")
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