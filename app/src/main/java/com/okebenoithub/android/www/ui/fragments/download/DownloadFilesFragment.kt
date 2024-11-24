package com.okebenoithub.android.www.ui.fragments.download

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.components.adapters.DownloadsListAdapter
import com.okebenoithub.android.www.components.managers.WrapCustomLinearLayoutManager
import com.okebenoithub.android.www.databinding.FragmentDownloadFilesBinding
import com.okebenoithub.android.www.ui.activities.MainActivity
import com.okebenoithub.android.www.ui.fragments.download.viewModel.DownloadFilesViewModel

class DownloadFilesFragment : Fragment() {

    private var _binding: FragmentDownloadFilesBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var downloadsListAdapter: DownloadsListAdapter

    private fun requestPermissionLauncher(viewModel: DownloadFilesViewModel): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.isLocalPermissionsGranted = true
                } else {
                    // Handle permission denied case
                    // show a message
                    (activity as MainActivity).mainUtil.showToastMessage(
                        requireContext(),
                        "Permission denied"
                    )
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadFilesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        // init view model
        val viewModel: DownloadFilesViewModel by activityViewModels { (activity as MainActivity).downloadFilesVMFactory }
        binding.viewModel = viewModel

        // check if local permissions granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher(viewModel).launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else viewModel.isLocalPermissionsGranted = true

        // set up downloads list adapter
        setUpDownloadsListAdapter(viewModel)
        return binding.root
    }

    /**
     * Set up downloads list adapter
     * This method is used to set up downloads list adapter
     */
    private fun setUpDownloadsListAdapter(viewModel: DownloadFilesViewModel) {
        // init adapter
        downloadsListAdapter = DownloadsListAdapter(requireContext(), viewModel)

        // define decoration
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)

        // set adapter with layout manager and decoration
        binding.downloadFilesListRv.apply {
            adapter = downloadsListAdapter
            layoutManager = WrapCustomLinearLayoutManager(requireContext())
            addItemDecoration(decoration)
        }

        // submit downloads list
        downloadsListAdapter.differ.submitList(viewModel.downloadItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}