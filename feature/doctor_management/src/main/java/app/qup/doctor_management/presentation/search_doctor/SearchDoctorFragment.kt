package app.qup.doctor_management.presentation.search_doctor

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import app.qup.doctor_management.databinding.FragmentSearchDoctorBinding
import app.qup.ui.common.LoaderAdapter
import app.qup.ui.common.safeNavigate
import app.qup.util.common.onlyDigits
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchDoctorFragment : Fragment(), MenuProvider {
    private var _binding: FragmentSearchDoctorBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

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
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        callFunctions()
    }

    private fun callFunctions() {
        setupAdapter()
        searchDoctor()
        loadDoctors()
        addDoctor()


    }

    private fun setupAdapter() {
        doctorAdapter = DoctorAdapter()
        binding.rvDoctors.adapter = doctorAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
        doctorAdapter.onItemClick = { doctorR ->
            val action = SearchDoctorFragmentDirections.actionSearchDoctorFragmentToAddDoctorFragment(
                mobileNumber = doctorR.mobileNumber.toString(),
                doctorId = doctorR.doctorId,
            )
            navController?.safeNavigate(action)
        }
    }

    private fun searchDoctor() {
        binding.etNameNumber.addTextChangedListener {
            val query = it.toString()
            if (query.isEmpty()) {
                // Load New Data
                searchDoctorViewModel.getAllDoctors(size = 10)
            } else {
                if (query.onlyDigits() && query.length == 10) {
                    binding.etNameNumber.filters = arrayOf(InputFilter.LengthFilter(10))
                    searchDoctorViewModel.searchDoctorByNumber(query)
                    // Set New Data
                    searchDoctorViewModel.searchDoctorByNumber.observe(viewLifecycleOwner) { searchDoctorByNameState ->
                        searchDoctorByNameState.doctorR?.let { doctor ->
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
                    binding.etNameNumber.filters = arrayOf(InputFilter.LengthFilter(Int.MAX_VALUE))
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

    private fun addDoctor() {
        binding.btnAdd.setOnClickListener {
            val action = SearchDoctorFragmentDirections.actionSearchDoctorFragmentToAddDoctorFragment()
            navController?.safeNavigate(action)
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