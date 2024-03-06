package app.qup.srk_management.presentation.add_srk

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
import app.qup.srk_management.data.remote.dto.request.AddSrkRequestDto
import app.qup.srk_management.databinding.FragmentAddSrkBinding
import app.qup.srk_management.domain.model.Srk
import app.qup.ui.common.snack
import app.qup.util.common.LOCAL_DATE_FORMAT
import app.qup.util.common.UserRole
import app.qup.util.common.millisToDateString
import app.qup.util.common.transformIntoDatePicker
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AddSrkFragment : Fragment(), MenuProvider {
    private var _binding: FragmentAddSrkBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }
    private val args by navArgs<AddSrkFragmentArgs>()

    private val addSrkViewModel by viewModels<AddSrkViewModel>()

    private var selectedGender : String? = null
    private var selectedBloodGroup : String? = null
    private var selectedBloodType : String? = null

    private val mSDF = SimpleDateFormat(LOCAL_DATE_FORMAT, Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSrkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        callFunctions()
    }

    private fun callFunctions() {
        binding.etDob.transformIntoDatePicker(requireContext(), LOCAL_DATE_FORMAT)
        loadGenders()
        addReception()
        bloodGroupLister()

        getUserData()

        binding.etMobileNumber.isEnabled = args.mobileNumber == null
        args.mobileNumber?.let {
            binding.etMobileNumber.setText(args.mobileNumber)
        }
        binding.btnSubmit.text = if (args.mobileNumber == null) "Submit" else "Update"

        etMobileListener()
    }

    private fun getUserData() {
        args.mobileNumber?.let {
            addSrkViewModel.getUser(it)
        }
        addSrkViewModel.getUser.observe(viewLifecycleOwner) {
            it.srk?.let { srk ->
                fillUserData(srk)
            }
        }
    }

    private fun bloodGroupLister() {
        binding.tgBloodGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val tb = group.findViewById<MaterialButton>(checkedId)
                selectedBloodGroup = tb.text.toString()
            }
        }
        binding.tgBloodType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val tb = group.findViewById<MaterialButton>(checkedId)
                selectedBloodType = tb.text.toString()
            }
        }
    }

    private fun loadGenders() {
        addSrkViewModel.genders.observe(viewLifecycleOwner) {
            it.genders?.let {  genders ->
                genders.forEach { gender ->
                    val rb = RadioButton(requireContext()).apply {
                        text = gender
                    }
                    binding.rgGender.addView(rb)
                }
            }
        }

        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById<RadioButton>(checkedId)
            if (rb != null) {
                selectedGender = rb.text.toString()
            }
        }
    }

    private fun addReception() {
        binding.btnSubmit.setOnClickListener {
            if (isValidForm()) {
                val mobileNumber = binding.etMobileNumber.text.toString()
                val firstName = binding.etFirstName.text.toString()
                val lastName = binding.etLastName.text.toString()
                val emailId = binding.etEmail.text.toString()
                val dobText = binding.etDob.text.toString()

                args.mobileNumber?.let {
                    addSrkViewModel.updateUser(
                        mobileNumber = args.mobileNumber ?: "",
                        addSrkRequestDto = AddSrkRequestDto(
                            bloodGroup = "$selectedBloodGroup$selectedBloodType",
                            dateOfBirth = if (dobText.isEmpty()) null else DateTime(mSDF.parse(dobText), DateTimeZone.UTC).toString(),
                            emailId = emailId.ifEmpty { null },
                            firstName = firstName,
                            lastName = lastName,
                            gender = selectedGender,
                            mobileNumber = mobileNumber.toLong(),
                            requestedRole = UserRole.VIRTUAL_RECEPTION.name,
                        )
                    )
                } ?: run {
                    addSrkViewModel.addSrk(
                        addSrkRequestDto = AddSrkRequestDto(
                            bloodGroup = "$selectedBloodGroup$selectedBloodType",
                            dateOfBirth = if (dobText.isEmpty()) null else DateTime(mSDF.parse(dobText), DateTimeZone.UTC).toString(),
                            emailId = emailId.ifEmpty { null },
                            firstName = firstName,
                            lastName = lastName,
                            gender = selectedGender,
                            mobileNumber = mobileNumber.toLong(),
                            requestedRole = UserRole.VIRTUAL_RECEPTION.name,
                        )
                    )
                }
            }
        }
        addSrkViewModel.addSrk.observe(viewLifecycleOwner) {
            it.srk?.let {
                navController?.popBackStack()
            }
        }
        addSrkViewModel.updateUser.observe(viewLifecycleOwner) {
            it.srk?.let {
                navController?.popBackStack()
            }
        }
    }

    private fun isValidForm(): Boolean {
        if (binding.etMobileNumber.text.toString().length != 10) {
            binding.root.snack("Enter Valid Mobile No.")
            binding.etMobileNumber.error = "Required"
            binding.etMobileNumber.requestFocus()
            return false
        }
        if (binding.etFirstName.text.isEmpty()) {
            binding.root.snack("Enter First Name")
            binding.etFirstName.error = "Required"
            binding.etFirstName.requestFocus()
            return false
        }
        if (binding.etLastName.text.isEmpty()) {
            binding.root.snack("Enter Last Name")
            binding.etLastName.error = "Required"
            binding.etLastName.requestFocus()
            return false
        }
        if (selectedGender == null) {
            binding.root.snack("Select Gender")
            return false
        }
        if (binding.etEmail.text.toString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
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

    private fun etMobileListener() {
        binding.etMobileNumber.addTextChangedListener {
            val query = it.toString()
            if (query.length == 10) {
                addSrkViewModel.checkRoleEligibility(
                    mobileNumber = query,
                    role = UserRole.VIRTUAL_RECEPTION.name
                )
            } else {
                binding.etFirstName.setText("")
                binding.etLastName.setText("")
                binding.rgGender.clearCheck()
                binding.etDob.setText("")
                binding.etEmail.setText("")
                binding.tgBloodGroup.clearChecked()
                binding.tgBloodType.clearChecked()

                binding.lblError.isVisible = false
            }
        }
        addSrkViewModel.roleEligibility.observe(viewLifecycleOwner) {
            it.srk?.let { srk ->
                fillUserData(srk)
            }
            it.error?.let { error ->
                if (it.errorCode == 409) {
                    binding.lblError.isVisible = true
                    binding.lblError.text = error
                } else {
                    binding.lblError.isVisible = false
                }
            }
        }
    }

    private fun fillUserData(srk: Srk) {
        binding.etFirstName.setText(srk.firstName)
        binding.etLastName.setText(srk.lastName)

        val allRadioButtons = mutableListOf<RadioButton>()
        for (i in 0 until binding.rgGender.childCount) {
            allRadioButtons.add(binding.rgGender.getChildAt(i) as RadioButton)
        }

        allRadioButtons.forEach { radioButton ->
            radioButton.isChecked = radioButton.text == srk.gender
        }

        binding.etDob.setText(millisToDateString(srk.dateOfBirth))
        binding.etEmail.setText(srk.emailId)

        val bloodGroup = srk.bloodGroup
        val group = bloodGroup.replace("+", "").replace("-", "")
        if (bloodGroup.isNotEmpty())
            binding.tgBloodGroup.check(
                when (group) {
                    "A" -> binding.optionA.id
                    "B" -> binding.optionB.id
                    "AB" -> binding.optionAb.id
                    "O" -> binding.optionO.id
                    else -> -1
                }
            )
        if (bloodGroup.contains("+")) {
            binding.tgBloodType.check(binding.optionPositive.id)
        } else if (bloodGroup.contains("-")) {
            binding.tgBloodType.check(binding.optionNegative.id)
        } else {
            binding.tgBloodType.check(-1)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                navController?.popBackStack()
                true
            }
            else -> false
        }
    }
}