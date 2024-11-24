package com.okebenoithub.android.www.ui.fragments.download.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DownloadFilesVMFactory(
    private val app: Application,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DownloadFilesViewModel(
            app
        ) as T
    }
}