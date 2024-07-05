package com.okebenoithub.android.www.di.core

import android.app.Application
import com.okebenoithub.android.www.ui.fragments.home.viewModel.HomeViewModelFactory
import com.okebenoithub.android.www.ui.fragments.project.viewModel.ProjectsViewModelFactory
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
}








