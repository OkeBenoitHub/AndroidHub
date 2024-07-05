package com.okebenoithub.android.www.ui.fragments.datastore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.okebenoithub.android.www.databinding.FragmentDataStoreBinding

class DataStoreFragment : Fragment() {
    private lateinit var viewModel: DataStoreViewModel
    private lateinit var viewModelFactory: DataStoreViewModelFactory
    private lateinit var binding: FragmentDataStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataStoreBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // TODO: Initialize ViewModel
        viewModelFactory = DataStoreViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)[DataStoreViewModel::class.java]

        binding.viewModel = viewModel

        // TODO: Observe UI state
        viewModel.uiState.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}