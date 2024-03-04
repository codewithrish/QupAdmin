package app.qup.doctor_management.presentation.search_doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import app.qup.doctor_management.databinding.FragmentSearchDoctorBinding
import app.qup.ui.common.LoaderAdapter
import app.qup.util.common.onlyDigits
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchDoctorFragment : Fragment() {
    private var _binding: FragmentSearchDoctorBinding? = null
    private val binding get() = _binding!!

    private lateinit var doctorAdapter: DoctorAdapter
    private val searchDoctorViewModel by viewModels<SearchDoctorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }

    private fun callFunctions() {
        setupAdapter()
        searchDoctor()
        loadDoctors()
    }

    private fun setupAdapter() {
        doctorAdapter = DoctorAdapter()
        binding.rvDoctors.adapter = doctorAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
    }

    private fun searchDoctor() {
        binding.etNameNumber.addTextChangedListener {
            val query = it.toString()
            if (query.isEmpty()) {
                // Load New Data
                searchDoctorViewModel.getAllDoctors(size = 10)
            } else {
                if (query.onlyDigits() && query.length == 10) {
                    searchDoctorViewModel.searchDoctorByNumber(query)
                    // Set New Data
                    searchDoctorViewModel.searchDoctorByNumber.observe(viewLifecycleOwner) { searchDoctorByNameState ->
                        searchDoctorByNameState.doctor?.let { doctor ->
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    // Clear Adapter
                                    doctorAdapter.submitData(PagingData.empty())
                                    doctorAdapter.submitData(PagingData.from(listOf(doctor)))
                                }
                            }
                        }
                    }
                } else {
                    if (!query.onlyDigits()) {
                        searchDoctorViewModel.searchDoctorByName(query, 10)
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                // Clear Adapter
                                doctorAdapter.submitData(PagingData.empty())
                                // Set New Data
                                searchDoctorViewModel.searchDoctors.collectLatest { pagingData ->
                                    doctorAdapter.submitData(pagingData)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadDoctors() {
        // Load New Data
        searchDoctorViewModel.getAllDoctors(size = 10)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Clear Adapter
                doctorAdapter.submitData(PagingData.empty())
                // Set New Data
                searchDoctorViewModel.doctors.collectLatest {
                    doctorAdapter.submitData(it)
                }
            }
        }
    }
}