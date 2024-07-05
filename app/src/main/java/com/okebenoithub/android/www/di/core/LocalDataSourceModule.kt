package com.okebenoithub.android.www.di.core

import com.okebenoithub.android.www.data.sqlite.project.ProjectsDAO
import com.okebenoithub.android.www.data.sqlite.user.UsersDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * LocalDataSourceModule
 * This module provides the LocalDataSource implementation
 */
@Module
@InstallIn(SingletonComponent::class)
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideUserLocalDataSource(usersDAO: UsersDAO) =
        com.okebenoithub.android.www.data.datasourcesImpl.user.LocalDataSourceImpl(usersDAO)

    @Singleton
    @Provides
    fun provideProjectLocalDataSource(projectsDAO: ProjectsDAO) =
        com.okebenoithub.android.www.data.datasourcesImpl.project.LocalDataSourceImpl(projectsDAO)
}













