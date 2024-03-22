package app.qup.summary.presentation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.qup.summary.data.remote.dto.general.OpdStatusValue
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.databinding.FragmentQueueSummaryBinding
import app.qup.summary.domain.model.CustomSummary
import app.qup.summary.domain.model.Summary
import app.qup.util.common.LOCAL_DATE_FORMAT
import app.qup.util.common.getMidNightTime
import app.qup.util.common.millisToDateString
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.Calendar

@AndroidEntryPoint
class QueueSummaryFragment : Fragment(), MenuProvider {
    private var _binding: FragmentQueueSummaryBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    private val queueSummaryViewModel by viewModels<QueueSummaryViewModel>()

    private lateinit var summaryAdapter: SummaryAdapter
    private lateinit var customSummaryAdapter: CustomSummaryAdapter

    private var summaryList: List<Summary> = mutableListOf()
    private var customSummaryList: List<CustomSummary> = mutableListOf()

    private var searchQuery = ""
    private var currentSelectedDate: Long = getMidNightTime()

    private var selectedOpdStatusValue: OpdStatusValue? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueueSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        callFunctions()
    }

    private fun callFunctions() {
        setupSummaryAdapter()
        setupCustomSummaryAdapter()
        normalCustomListener()
        setupDate()
        searchByDoctorListener()
        clearSearch()
        binding.tgSummaryType.check(binding.optionNormal.id)
    }

    private fun getOpdStatusValues() {
        when (binding.tgSummaryType.checkedButtonId) {
            binding.optionNormal.id -> queueSummaryViewModel.getOpdStatusValues()
            binding.optionCustom.id -> queueSummaryViewModel.getCustomOpdStatusValues()
        }
        queueSummaryViewModel.opdStatusValues.observe(viewLifecycleOwner) {
            it.values?.let { values ->
                populateSpinner(values)
            }
        }
        queueSummaryViewModel.customOpdStatusValues.observe(viewLifecycleOwner) {
            it.values?.let { values ->
                populateSpinner(values)
            }
        }
    }

    private fun populateSpinner(values: Map<String, OpdStatusValue>) {
        val finalValues = values.map { it1 -> it1.value }.toMutableList()
        finalValues.add(0, OpdStatusValue(displayName = "Select OPD Status"))
        val opdStatusValuesAdapter: ArrayAdapter<OpdStatusValue?> =
            object : ArrayAdapter<OpdStatusValue?>(requireContext(), app.qup.ui.R.layout.list_item_array_adapter, finalValues.toList()) {
                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getView(position, convertView, parent) as TextView
                    view.text = getItem(position)?.displayName
                    return view
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getView(position, convertView, parent) as TextView
                    view.setPadding(16,16,16,16)
                    view.text = getItem(position)?.displayName
                    return view
                }
            }
        binding.spValues.adapter = opdStatusValuesAdapter
        binding.spValues.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedOpdStatusValue =  if (position == 0) null else opdStatusValuesAdapter.getItem(position) as OpdStatusValue
                updateAdapterData()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun searchByDoctorListener() {
        binding.etDoctorName.addTextChangedListener {
            searchQuery = it.toString().lowercase()
            updateAdapterData()
        }
    }

    private fun updateAdapterData() {
        val checkedId = binding.tgSummaryType.checkedButtonId
        when (checkedId) {
            binding.optionNormal.id -> {
                val filteredList = if (searchQuery.isEmpty() && selectedOpdStatusValue == null) {
                    summaryList
                } else {
                    summaryList.filter { it1 ->
                        it1.doctorFullName.lowercase().contains(searchQuery) &&
                        it1.businessSideOPDSlotStatus == selectedOpdStatusValue?.name
                    }
                }
                binding.tvNoRecords.isVisible = filteredList.isEmpty()
                summaryAdapter.submitList(filteredList)
            }
            binding.optionCustom.id -> {
                val filteredList = if (searchQuery.isEmpty() && selectedOpdStatusValue == null) {
                    customSummaryList
                } else {
                    customSummaryList.filter { it1 ->
                        it1.doctorFullName.lowercase().contains(searchQuery) &&
                                it1.businessSideOPDSlotStatus == selectedOpdStatusValue?.name
                    }
                }
                binding.tvNoRecords.isVisible = filteredList.isEmpty()
                customSummaryAdapter.submitList(filteredList)
            }
        }
    }

    private fun normalCustomListener() {
        binding.tgSummaryType.addOnButtonCheckedListener { _, _, isChecked ->
            if (isChecked) {
                getOpdStatusValues()
                loadSummary()
            }
        }
    }

    private fun setupDate() {
        binding.tvDate.text = millisToDateString(getMidNightTime(), LOCAL_DATE_FORMAT)

        val myCalendar = Calendar.getInstance()

        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                myCalendar.set(Calendar.HOUR_OF_DAY, 0)
                myCalendar.set(Calendar.MINUTE, 0)
                myCalendar.set(Calendar.SECOND, 0)
                myCalendar.set(Calendar.MILLISECOND, 0)

                currentSelectedDate = myCalendar.timeInMillis

                loadSummary()

                binding.tvDate.text = millisToDateString(myCalendar.timeInMillis, LOCAL_DATE_FORMAT)
            }

        binding.tvDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                // datePicker.minDate = Calendar.getInstance().timeInMillis
                show()
            }
        }
    }

    private fun setupSummaryAdapter() {
        summaryAdapter = SummaryAdapter()
        binding.rvSummary.adapter = summaryAdapter
    }

    private fun setupCustomSummaryAdapter() {
        customSummaryAdapter = CustomSummaryAdapter()
        binding.rvSummary.adapter = customSummaryAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun loadSummary() {
        when (binding.tgSummaryType.checkedButtonId) {
            binding.optionNormal.id -> {
                queueSummaryViewModel.getQueueSummary(
                    summaryRequestDto = SummaryRequestDto(
                        bookingDailySlotDatePerUTCMidnight = DateTime(currentSelectedDate, DateTimeZone.UTC).toString()
                    )
                )
            }
            binding.optionCustom.id -> {
                queueSummaryViewModel.getCustomQueueSummary(
                    summaryRequestDto = SummaryRequestDto(
                        bookingDailySlotDatePerUTCMidnight = DateTime(currentSelectedDate, DateTimeZone.UTC).toString()
                    )
                )
            }
        }
        queueSummaryViewModel.queueSummary.observe(viewLifecycleOwner) {
            it.summaryList?.let { summaryList ->
                binding.tvTotalOnlineBookingsMade.text = "Total Online Bookings Made: ${summaryList.sumOf { it1 -> it1.noOfOnlineBookingsMade }}"
                binding.tvTotalWalkInBookingsMade.text = "Total WalkIn Bookings Made: ${summaryList.sumOf { it1 -> (it1.totalNoOfBookingsMade - it1.noOfOnlineBookingsMade) }}"
                setupSummaryAdapter()
                this.summaryList = summaryList
                summaryAdapter.submitList(summaryList)
            }
            binding.progressBar.isVisible = it.isLoading
        }
        queueSummaryViewModel.customQueueSummary.observe(viewLifecycleOwner) {
            it.customSummaryList?.let { customSummaryList ->
                binding.tvTotalOnlineBookingsMade.text = "Total Online Bookings Made: ${customSummaryList.sumOf { it1 -> it1.noOfOnlineBookingsMade }}"
                binding.tvTotalWalkInBookingsMade.text = "Total WalkIn Bookings Made: ${customSummaryList.sumOf { it1 -> (it1.totalNoOfBookingsAllowed - it1.noOfOnlineBookingsMade) }}"
                setupCustomSummaryAdapter()
                this.customSummaryList = customSummaryList
                customSummaryAdapter.submitList(customSummaryList)
            }
            binding.progressBar.isVisible = it.isLoading
        }
    }

    private fun clearSearch() {
        binding.btnClearSearch.setOnClickListener {
            binding.etDoctorName.setText("")
            binding.spValues.setSelection(0)
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