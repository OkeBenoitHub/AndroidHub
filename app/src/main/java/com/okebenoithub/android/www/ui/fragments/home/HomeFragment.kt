package com.okebenoithub.android.www.ui.fragments.home

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.databinding.FragmentHomeBinding
import com.okebenoithub.android.www.ui.activities.MainActivity
import com.okebenoithub.android.www.ui.fragments.home.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // init view model
        val homeViewModel: HomeViewModel by activityViewModels { (activity as MainActivity).homeViewModelFactory }
        binding.lifecycleOwner = this
        binding.viewModel = homeViewModel

        // open github
        binding.goToGitHubBtn.setOnClickListener {
            (activity as MainActivity).mainUtil.openWebPage(
                requireContext(),
                getString(R.string.github_link)
            )
        }

        // go to Cv fragment
        binding.openCVBtnLayout.setOnClickListener {
            (activity as MainActivity).findNavController(
                R.id.fragmentContainerView
            ).navigate(R.id.action_homeFragment_to_myCvFragment)
        }

        // go to projects fragment
        binding.goToProjectsFragmentBtn.setOnClickListener {
            (activity as MainActivity).findNavController(
                R.id.fragmentContainerView
            ).navigate(R.id.action_homeFragment_to_projectsFragment)
        }

        // hire me button tapped
        binding.hireMeBtn.setOnClickListener {
            (activity as MainActivity).mainUtil.composeEmail(
                requireContext(),
                arrayOf(getString(R.string.email_app_for_reports_and_feedback)),
                getString(R.string.hire_me_text),
                getString(R.string.hire_me_message),
                getString(R.string.hire_me_via_text)
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add menu item to fragment
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
                menuInflater.inflate(R.menu.home_menu, menu)

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                when (menuItem.itemId) {
                    // feedback
                    R.id.feedback -> {
                        (activity as MainActivity).mainUtil.composeEmail(
                            requireContext(),
                            arrayOf(getString(R.string.email_app_for_reports_and_feedback)),
                            getString(R.string.feedback_text),
                            getString(R.string.tell_us_how_to_improve_app),
                            getString(R.string.send_feedback_via_text)
                        )
                    }

                    // report issue
                    R.id.report_issue -> {
                        (activity as MainActivity).mainUtil.composeEmail(
                            requireContext(),
                            arrayOf(getString(R.string.email_app_for_reports_and_feedback)),
                            getString(R.string.report_issue_text),
                            getString(R.string.tell_us_about_issue_text),
                            getString(R.string.report_issue_via_text)
                        )
                    }

                    // share app
                    R.id.share_app -> {
                        var aboutAppMessage = getString(R.string.about_app_share_1)
                        aboutAppMessage += "\n" + getString(R.string.app_store_link)
                        (activity as MainActivity).mainUtil.shareTextData(
                            requireActivity(),
                            getString(R.string.share_app_via_text),
                            aboutAppMessage
                        )
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}