package app.qup.reception_management.presentation.search_reception

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
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.reception_management.databinding.FragmentSearchReceptionBinding
import app.qup.ui.common.LoaderAdapter
import app.qup.util.common.UserRole
import app.qup.util.common.onlyDigits
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchReceptionFragment : Fragment() {
    private var _binding: FragmentSearchReceptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var receptionPaginatedAdapter: ReceptionPaginatedAdapter
    private lateinit var receptionAdapter: ReceptionAdapter
    private val searchReceptionViewModel by viewModels<SearchReceptionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchReceptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }

    private fun callFunctions() {
        setupPaginatedAdapter()
        searchUser()
        loadReceptions()
    }

    private fun setupAdapter() {
        receptionAdapter = ReceptionAdapter()
        binding.rvReceptions.adapter = receptionAdapter
    }

    private fun setupPaginatedAdapter() {
        receptionPaginatedAdapter = ReceptionPaginatedAdapter()
        binding.rvReceptions.adapter = receptionPaginatedAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
    }

    private fun searchUser() {
        binding.etNameNumber.addTextChangedListener {
            val query = it.toString()
            if (query.isEmpty()) {
                setupPaginatedAdapter()
                loadReceptions()
            } else {
                setupAdapter()
                if (query.onlyDigits() && query.length == 10) {
                    searchReceptionViewModel.searchUserByNumber(
                        role = UserRole.RECEPTIONIST.name,
                        mobileNumber = query
                    )
                    searchReceptionViewModel.searchedReception.observe(viewLifecycleOwner) { searchedUserByNameState ->
                        searchedUserByNameState.reception?.let { reception ->
                            receptionAdapter.submitList(listOf(reception))
                        }
                    }
                } else {
                    if (!query.onlyDigits()) {
                        val firstName = if (query.contains(" ")) query.split(" ")[0] else query
                        val lastName = if (query.contains(" ")) query.split(" ")[1] else ""
                        searchReceptionViewModel.searchUserByName(
                            searchUserRequestDto = SearchUserRequestDto(
                                appRole = UserRole.RECEPTIONIST.name,
                                firstName = firstName,
                                lastName = lastName
                            )
                        )
                        searchReceptionViewModel.searchedReceptions.observe(viewLifecycleOwner) { searchedUserByNameState ->
                            searchedUserByNameState.receptions?.let { receptions ->
                                receptionAdapter.submitList(receptions)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadReceptions() {
        // Load New Data
        searchReceptionViewModel.getAllReceptions(role = UserRole.RECEPTIONIST.name, size = 10)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Clear Adapter
                receptionPaginatedAdapter.submitData(PagingData.empty())
                // Set New Data
                searchReceptionViewModel.receptions.collectLatest {
                    receptionPaginatedAdapter.submitData(it)
                }
            }
        }
    }
}