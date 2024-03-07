package app.qup.doctor_management.presentation.add_doctor

import android.R
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.qup.doctor_management.databinding.FragmentAddDoctorBinding
import app.qup.doctor_management.domain.model.Degree
import app.qup.doctor_management.domain.model.Speciality
import app.qup.doctor_management.domain.model.SpecialityCategory
import app.qup.ui.common.snack
import app.qup.util.common.LOCAL_DATE_FORMAT
import app.qup.util.common.transformIntoDatePicker
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
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
        // Step 3
        populateSpecialityCategory()
        populateSpeciality()
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
        val yearsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_dropdown_item, years)
        binding.layoutStep2.spYear.setAdapter(yearsAdapter)
    }

    private fun populateMonths() {
        val months = getMonths()
        val monthsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_dropdown_item, months)
        binding.layoutStep2.spMonth.setAdapter(monthsAdapter)
    }

    private fun populateDegrees() {
        addDoctorViewModel.degrees.observe(viewLifecycleOwner) {
            it.degrees?.let { degrees ->
                val finalDegrees: MutableList<Degree> = degrees.toMutableList()
                finalDegrees.add(0, Degree(name = "Select Degree"))
                val degreesAdapter: ArrayAdapter<Degree?> =
                    object : ArrayAdapter<Degree?>(requireContext(), app.qup.ui.R.layout.list_item_array_adapter, finalDegrees.toList()) {
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
            }
        }
    }

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
            }
        }
    }
    private fun populateSpeciality() {
        addDoctorViewModel.specialities.observe(viewLifecycleOwner) {
            it.specialities?.let { specialities ->
                val finalSpecialities: MutableList<Speciality> = specialities.toMutableList()
                finalSpecialities.add(0, Speciality(name = "Select Speciality"))
                val degreesAdapter: ArrayAdapter<Speciality?> =
                    object : ArrayAdapter<Speciality?>(requireContext(), app.qup.ui.R.layout.list_item_array_adapter, finalSpecialities.toList()) {
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
            }
        }
    }
    private fun stepNumberObserver() {
        binding.btnNext.setOnClickListener {
            val currentValue = addDoctorViewModel.stepNumber.value!!
            if (currentValue == 1 /*&& isForm1Valid()*/) {
                addDoctorViewModel.stepNumber.postValue(2)
            }
            if (currentValue == 2 && isForm2Valid()) {
                addDoctorViewModel.stepNumber.postValue(3)
            }
            if (currentValue == 3 && isForm3Valid()) {
                addDoctorViewModel.stepNumber.postValue(4)
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
            binding.btnNext.isVisible = it != 4

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
        return true
    }

    private fun isForm3Valid(): Boolean {
        return true
    }

    private fun isForm4Valid(): Boolean {
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
            R.id.home -> {
                val currentStep = addDoctorViewModel.stepNumber.value!!
                if (currentStep > 1)
                    addDoctorViewModel.stepNumber.postValue(currentStep-1)
                else
                    navController?.popBackStack()
                true
            }
            else -> false
        }
    }
}