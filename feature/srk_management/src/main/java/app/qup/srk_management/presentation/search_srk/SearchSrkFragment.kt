package app.qup.srk_management.presentation.search_srk

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
import androidx.paging.PagingData
import app.qup.srk_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.srk_management.databinding.FragmentSearchSrkBinding
import app.qup.ui.common.LoaderAdapter
import app.qup.ui.common.safeNavigate
import app.qup.util.common.UserRole
import app.qup.util.common.onlyDigits
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchSrkFragment : Fragment(), MenuProvider {
    private var _binding: FragmentSearchSrkBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

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
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        callFunctions()
    }

    private fun callFunctions() {
        setupPaginatedAdapter()
        searchUser()
        loadSrks()
        addSrk()
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
                    binding.etNameNumber.filters = arrayOf(InputFilter.LengthFilter(10))
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
                    binding.etNameNumber.filters = arrayOf(InputFilter.LengthFilter(Int.MAX_VALUE))
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

    private fun addSrk() {
        binding.btnAdd.setOnClickListener {
            val action = SearchSrkFragmentDirections.actionSearchSrkFragmentToAddSrkFragment()
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