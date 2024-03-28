package app.qup.indiapps.presentation

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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.qup.indiapps.databinding.FragmentIndiAppsBinding
import app.qup.indiapps.domain.model.IndiApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndiAppsFragment : Fragment(), MenuProvider {
    private var _binding: FragmentIndiAppsBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    private val indiAppsViewModel by viewModels<IndiAppsViewModel>()

    private val allIndiApps: MutableList<IndiApp> = mutableListOf()
    private lateinit var indiAppsAdapter: IndiAppsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndiAppsBinding.inflate(inflater, container, false)
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
        searchListener()
        loadIndiApps()
    }

    private fun searchListener() {
        binding.etIndiappName.addTextChangedListener {
            val query = it.toString().trim().lowercase()
            val filteredList = if (query.isEmpty()) allIndiApps
            else allIndiApps.filter { it1 -> it1.appName.trim().lowercase().contains(query) }
            indiAppsAdapter.submitList(filteredList)
            binding.lblNoRecords.isVisible = filteredList.isEmpty()
        }
    }

    private fun loadIndiApps() {
        indiAppsViewModel.getIndiApps()
        indiAppsViewModel.indiApps.observe(viewLifecycleOwner) {
            it.indiApps?.let { indiApps ->
                binding.lblNoRecords.isVisible = indiApps.isEmpty()
                allIndiApps.clear()
                allIndiApps.addAll(indiApps)
                indiAppsAdapter.submitList(indiApps)
            }
            binding.progressBar.isVisible = it.isLoading
        }
    }

    private fun setupAdapter() {
        indiAppsAdapter = IndiAppsAdapter()
        binding.rvIndiapps.adapter = indiAppsAdapter
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