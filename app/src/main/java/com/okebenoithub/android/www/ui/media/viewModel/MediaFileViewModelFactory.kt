package com.okebenoithub.android.www.ui.media.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MediaFileViewModelFactory(
    private val app: Application,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaFileViewModel(
            app
        ) as T
    }
}