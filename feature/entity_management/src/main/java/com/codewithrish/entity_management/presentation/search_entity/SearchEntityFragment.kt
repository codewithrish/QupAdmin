package com.codewithrish.entity_management.presentation.search_entity

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
import app.qup.ui.common.LoaderAdapter
import com.codewithrish.entity_management.databinding.FragmentSearchEntityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchEntityFragment : Fragment() {
    private var _binding: FragmentSearchEntityBinding? = null
    private val binding get() = _binding!!

    private lateinit var entityAdapter: EntityAdapter
    private val searchEntityViewModel by viewModels<SearchEntityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchEntityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }

    private fun callFunctions() {
        setupAdapter()
        searchEntities()
        loadEntities()
    }

    private fun setupAdapter() {
        entityAdapter = EntityAdapter()
        binding.rvEntities.adapter = entityAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
    }

    private fun searchEntities() {
        binding.etEntityName.addTextChangedListener {
            val query = it.toString()
            if (query.isNotEmpty()) {
                searchEntityViewModel.searchEntities(
                    entityNames = listOf(query),
                    size = 10
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        // Clear Adapter
                        entityAdapter.submitData(PagingData.empty())
                        // Set New Data
                        searchEntityViewModel.searchedEntities.collectLatest {
                            entityAdapter.submitData(it)
                        }
                    }
                }
            } else {
                loadEntities()
            }
        }
    }

    private fun loadEntities() {
        // Load New Data
        searchEntityViewModel.getAllEntities(10)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Clear Adapter
                entityAdapter.submitData(PagingData.empty())
                // Set New Data
                searchEntityViewModel.entities.collectLatest {
                    entityAdapter.submitData(it)
                }
            }
        }
    }


}