package com.okebenoithub.android.www.di.core

import android.app.Application
import com.okebenoithub.android.www.ui.fragments.download.viewModel.DownloadFilesVMFactory
import com.okebenoithub.android.www.ui.fragments.home.viewModel.HomeViewModelFactory
import com.okebenoithub.android.www.ui.fragments.project.viewModel.ProjectsViewModelFactory
import com.okebenoithub.android.www.ui.media.viewModel.MediaFileViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * FactoryModule
 * This module is responsible for providing the factories for the view models
 */
@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideHomeViewModelFactory(
       app: Application,
    ): HomeViewModelFactory {
        return HomeViewModelFactory(
            app
        )
    }

    @Singleton
    @Provides
    fun provideProjectsViewModelFactory(
        app: Application,
    ): ProjectsViewModelFactory {
        return ProjectsViewModelFactory(
            app
        )
    }

    @Singleton
    @Provides
    fun provideMediaFileViewModelFactory(
        app: Application,
    ): MediaFileViewModelFactory {
        return MediaFileViewModelFactory(
            app
        )
    }

    @Singleton
    @Provides
    fun provideDownloadFilesViewModelFactory(
        app: Application,
    ): DownloadFilesVMFactory {
        return DownloadFilesVMFactory(
            app
        )
    }
}








