package com.okebenoithub.android.www.ui.fragments.kotlinFlows

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.okebenoithub.android.www.databinding.FragmentKotlinFlowsBinding
import com.okebenoithub.android.www.utils.FlowUtil.asLiveData

class KotlinFlowsFragment : Fragment() {

    private val viewModel: KotlinFlowsViewModel by viewModels()
    private lateinit var binding: FragmentKotlinFlowsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKotlinFlowsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.collectFlowDataBtn.setOnClickListener { collectDataFlow() }
        return binding.root
    }

    private fun collectDataFlow() {
        viewModel.collectFlow()
        //viewModel.debounceAndThrottleFlow()
        //viewModel.collectCombinedFlow()
        viewModel.uiState.asLiveData(lifecycleScope).observe(viewLifecycleOwner) { uiState ->
            binding.flowDataTv.text = uiState.toString()
        }
    }
}