package app.qup.summary.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.databinding.FragmentQueueSummaryBinding
import app.qup.util.common.LOCAL_DATE_FORMAT
import app.qup.util.common.getMidNightTime
import app.qup.util.common.millisToDateString
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class QueueSummaryFragment : Fragment(), MenuProvider {
    private var _binding: FragmentQueueSummaryBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    private val queueSummaryViewModel by viewModels<QueueSummaryViewModel>()

    private lateinit var summaryAdapter: SummaryAdapter
    private lateinit var customSummaryAdapter: CustomSummaryAdapter

    private var currentSelectedDate: Long = getMidNightTime()
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
        loadSummary()
        normalCustomListener()
        setupDate()
    }

    private fun normalCustomListener() {
        binding.tgSummaryType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.optionNormal.id -> loadSummary()
                    binding.optionCustom.id -> loadCustomSummary()
                }
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

                when (binding.tgSummaryType.checkedButtonId) {
                    binding.optionNormal.id -> loadSummary()
                    binding.optionCustom.id -> loadCustomSummary()
                }

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

    private fun loadSummary() {
        queueSummaryViewModel.getQueueSummary(
            summaryRequestDto = SummaryRequestDto(
                bookingDailySlotDatePerUTCMidnight = DateTime(currentSelectedDate, DateTimeZone.UTC).toString()
            )
        )
        queueSummaryViewModel.queueSummary.observe(viewLifecycleOwner) {
            it.summaryList?.let { summaryList ->
                setupSummaryAdapter()
                summaryAdapter.submitList(summaryList)
            }
            binding.progressBar.isVisible = it.isLoading
        }
    }

    private fun loadCustomSummary() {
        queueSummaryViewModel.getCustomQueueSummary(
            summaryRequestDto = SummaryRequestDto(
                bookingDailySlotDatePerUTCMidnight = DateTime(currentSelectedDate, DateTimeZone.UTC).toString()
            )
        )
        queueSummaryViewModel.customQueueSummary.observe(viewLifecycleOwner) {
            it.customSummaryList?.let { customSummaryList ->
                setupCustomSummaryAdapter()
                customSummaryAdapter.submitList(customSummaryList)
            }
            binding.progressBar.isVisible = it.isLoading
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