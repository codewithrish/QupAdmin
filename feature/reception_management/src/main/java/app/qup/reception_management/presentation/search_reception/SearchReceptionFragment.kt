package app.qup.reception_management.presentation.search_reception

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
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.reception_management.databinding.FragmentSearchReceptionBinding
import app.qup.ui.common.LoaderAdapter
import app.qup.ui.common.safeNavigate
import app.qup.util.common.UserRole
import app.qup.util.common.onlyDigits
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchReceptionFragment : Fragment(), MenuProvider {
    private var _binding: FragmentSearchReceptionBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

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
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        callFunctions()
    }

    private fun callFunctions() {
        setupPaginatedAdapter()
        searchUser()
        loadReceptions()
        addReception()
    }

    private fun setupAdapter() {
        receptionAdapter = ReceptionAdapter()
        binding.rvReceptions.adapter = receptionAdapter
        receptionAdapter.onClick = {
            goToUpdate(it.mobileNumber.toString())
        }
    }

    private fun setupPaginatedAdapter() {
        receptionPaginatedAdapter = ReceptionPaginatedAdapter()
        binding.rvReceptions.adapter = receptionPaginatedAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
        receptionPaginatedAdapter.onClick = {
            goToUpdate(it.mobileNumber.toString())
        }
    }

    private fun goToUpdate(mobileNumber: String) {
        val action = SearchReceptionFragmentDirections.actionSearchReceptionFragmentToAddReceptionFragment(
            mobileNumber = mobileNumber
        )
        navController?.safeNavigate(action)
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
                    binding.etNameNumber.filters = arrayOf(InputFilter.LengthFilter(10))
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
                    binding.etNameNumber.filters = arrayOf(InputFilter.LengthFilter(Int.MAX_VALUE))
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

    private fun addReception() {
        binding.btnAdd.setOnClickListener {
            val action = SearchReceptionFragmentDirections.actionSearchReceptionFragmentToAddReceptionFragment()
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