package com.okebenoithub.android.www.ui.fragments.coroutines

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.databinding.FragmentCoroutinesBinding

class CoroutinesFragment : Fragment() {

    private val viewModel: CoroutinesViewModel by viewModels()
    private lateinit var binding: FragmentCoroutinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoroutinesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.getCoroutinesDataBtn.setOnClickListener { viewModel.launchWithConcurrently() }
        viewModel.uiState.observe(viewLifecycleOwner) {
            it?.let {
                binding.coroutinesTv.text = it
            }
        }

        binding.stopCoroutinesDataBtn.setOnClickListener { viewModel.cancelJob(viewModel.launchConcurrentlyJob) }

        return binding.root
    }
}