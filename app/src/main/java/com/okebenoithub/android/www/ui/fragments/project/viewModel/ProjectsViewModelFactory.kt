package com.okebenoithub.android.www.ui.fragments.project.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProjectsViewModelFactory(
    private val app: Application,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectsViewModel(
            app
        ) as T
    }
}