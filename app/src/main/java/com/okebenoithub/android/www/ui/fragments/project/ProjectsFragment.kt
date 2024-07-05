package com.okebenoithub.android.www.ui.fragments.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.components.adapters.ProjectsListAdapter
import com.okebenoithub.android.www.components.managers.WrapCustomLinearLayoutManager
import com.okebenoithub.android.www.data.models.ProjectModel
import com.okebenoithub.android.www.databinding.FragmentProjectsBinding
import com.okebenoithub.android.www.ui.activities.MainActivity
import com.okebenoithub.android.www.ui.fragments.project.viewModel.ProjectsViewModel

class ProjectsFragment : Fragment() {
    private var _binding: FragmentProjectsBinding? = null
    private lateinit var projectsListAdapter: ProjectsListAdapter

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
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        // init view model
        val projectsViewModel: ProjectsViewModel by activityViewModels { (activity as MainActivity).projectsViewModelFactory }
        binding.viewModel = projectsViewModel

        // set up projects list adapter
        setUpProjectsListAdapter()
        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * Set up projects list adapter
     * This method is used to set up projects list adapter
     */
    private fun setUpProjectsListAdapter() {
        // init adapter
        projectsListAdapter = ProjectsListAdapter(requireContext())

        // define decoration
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)

        // set adapter with layout manager and decoration
        binding.projectsListRv.apply {
            adapter = projectsListAdapter
            layoutManager = WrapCustomLinearLayoutManager(requireContext())
            addItemDecoration(decoration)
        }

        // submit projects list
        projectsListAdapter.differ.submitList(projectsListAdapter.getProjectsList())

        // Tap on each list item from projects list adapter
        projectsListAdapter.setOnItemClickListener { item ->
            when(item.projectId) {
                "1" -> navigateToLiveDataVmFragment(item)
                "2" -> navigateToFlowsFragment(item)
                "3" -> navigateToCoroutinesFragment(item)
                "4" -> navigateToDataStoreFragment(item)
            }
        }

        // set on item info button click listener
        projectsListAdapter.setOnItemInfoBtnClickListener { item ->
            // open project info web
            (activity as MainActivity).mainUtil.openWebPage(requireContext(), item.docLink)
        }
    }

    // navigate to LiveData and ViewModel fragment with item argument
    private fun navigateToLiveDataVmFragment(item: ProjectModel) {
        // navigate to LiveData and ViewModel fragment with item argument
        (activity as MainActivity).findNavController(
            R.id.fragmentContainerView
        ).navigate(ProjectsFragmentDirections.actionProjectsFragmentToLiveDataVmFragment(item))
    }

    // navigate to Flows fragment with item argument
    private fun navigateToFlowsFragment(item: ProjectModel) {
        // navigate to Flows fragment
        (activity as MainActivity).findNavController(
            R.id.fragmentContainerView
        ).navigate(ProjectsFragmentDirections.actionProjectsFragmentToKotlinFlowsFragment(item))
    }

    // navigate to Coroutines fragment with item argument
    private fun navigateToCoroutinesFragment(item: ProjectModel) {
        // navigate to Coroutines fragment
        (activity as MainActivity).findNavController(
            R.id.fragmentContainerView
        ).navigate(ProjectsFragmentDirections.actionProjectsFragmentToCoroutinesFragment(item))
    }

    // navigate to DataStore fragment with item argument
    private fun navigateToDataStoreFragment(item: ProjectModel) {
        // navigate to DataStore fragment
        (activity as MainActivity).findNavController(
            R.id.fragmentContainerView
        ).navigate(ProjectsFragmentDirections.actionProjectsFragmentToDataStoreFragment(item))
    }
}