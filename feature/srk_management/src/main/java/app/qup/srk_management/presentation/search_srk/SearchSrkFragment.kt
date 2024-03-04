package app.qup.srk_management.presentation.search_srk

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
import app.qup.srk_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.srk_management.databinding.FragmentSearchSrkBinding
import app.qup.ui.common.LoaderAdapter
import app.qup.util.common.UserRole
import app.qup.util.common.onlyDigits
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchSrkFragment : Fragment() {
    private var _binding: FragmentSearchSrkBinding? = null
    private val binding get() = _binding!!

    private lateinit var srkPaginatedAdapter: SrkPaginatedAdapter
    private lateinit var srkAdapter: SrkAdapter
    private val searchSrkViewModel by viewModels<SearchSrkViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSrkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }

    private fun callFunctions() {
        setupPaginatedAdapter()
        searchUser()
        loadSrks()
    }

    private fun setupAdapter() {
        srkAdapter = SrkAdapter()
        binding.rvSrks.adapter = srkAdapter
    }
    
    private fun setupPaginatedAdapter() {
        srkPaginatedAdapter = SrkPaginatedAdapter()
        binding.rvSrks.adapter = srkPaginatedAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
    }

    private fun searchUser() {
        binding.etNameNumber.addTextChangedListener {
            val query = it.toString()
            if (query.isEmpty()) {
                setupPaginatedAdapter()
                loadSrks()
            } else {
                setupAdapter()
                if (query.onlyDigits() && query.length == 10) {
                    searchSrkViewModel.searchUserByNumber(
                        role = UserRole.VIRTUAL_RECEPTION.name,
                        mobileNumber = query
                    )
                    searchSrkViewModel.searchedSrk.observe(viewLifecycleOwner) { searchedUserByNameState ->
                        searchedUserByNameState.srk?.let { srk ->
                            srkAdapter.submitList(listOf(srk))
                        }
                    }
                } else {
                    if (!query.onlyDigits()) {
                        val firstName = if (query.contains(" ")) query.split(" ")[0] else query
                        val lastName = if (query.contains(" ")) query.split(" ")[1] else ""
                        searchSrkViewModel.searchUserByName(
                            searchUserRequestDto = SearchUserRequestDto(
                                appRole = UserRole.VIRTUAL_RECEPTION.name,
                                firstName = firstName,
                                lastName = lastName
                            )
                        )
                        searchSrkViewModel.searchedSrkList.observe(viewLifecycleOwner) { searchedUserByNameState ->
                            searchedUserByNameState.srks?.let { srks ->
                                srkAdapter.submitList(srks)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadSrks() {
        // Load New Data
        searchSrkViewModel.getAllSrk(role = UserRole.VIRTUAL_RECEPTION.name, size = 10)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Clear Adapter
                srkPaginatedAdapter.submitData(PagingData.empty())
                // Set New Data
                searchSrkViewModel.srkList.collectLatest {
                    srkPaginatedAdapter.submitData(it)
                }
            }
        }
    }
}