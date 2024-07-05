package com.okebenoithub.android.www.ui.fragments.project.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ProjectsViewModel(private val app: Application) : AndroidViewModel(app) {
    // TODO: Implement the ViewModel
    val projectTitleEdt = MutableLiveData<String>()
}