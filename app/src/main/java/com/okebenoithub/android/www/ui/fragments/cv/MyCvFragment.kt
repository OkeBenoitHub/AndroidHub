package com.okebenoithub.android.www.ui.fragments.cv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.databinding.FragmentMyCvBinding
import com.okebenoithub.android.www.ui.activities.MainActivity

class MyCvFragment : Fragment() {
    private var _binding: FragmentMyCvBinding? = null
    private val myCvViewModel: MyCvViewModel by viewModels()
    private val cvPdfUrl = "https://firebasestorage.googleapis.com/v0/b/okebenoithub-a6607.appspot.com/o/resume_official_en%20(1).pdf?alt=media&token=ea2e503a-6d32-4c9a-ae29-c40489bb834d"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCvBinding.inflate(inflater, container, false)
        // init view model
        binding.lifecycleOwner = this
        binding.viewModel = myCvViewModel
        // display pdf CV file in view
        // show pdf viewer layout
        (activity as MainActivity).pdfUtil.loadPdfIntoView(binding.pdfView, cvPdfUrl, lifecycleScope, lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add menu item to fragment
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
                menuInflater.inflate(R.menu.my_cv_menu, menu)

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                when (menuItem.itemId) {
                    // download cv file
                    R.id.downloadCvFile -> (activity as MainActivity).mainUtil.openWebPage(requireContext(), cvPdfUrl)
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }
}