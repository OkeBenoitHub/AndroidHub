package com.okebenoithub.android.www.di.core
import com.okebenoithub.android.www.data.datasources.user.RemoteDataSource
import com.okebenoithub.android.www.data.datasourcesImpl.project.RemoteDataSourceImpl
import com.okebenoithub.android.www.data.firebase.ProjectDataManager
import com.okebenoithub.android.www.data.firebase.UserDataManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RemoteDataSourceModule
 * This module provides the RemoteDataSource implementation.
 */

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        usersDataManager: UserDataManager
    ): RemoteDataSource =
        com.okebenoithub.android.www.data.datasourcesImpl.user.RemoteDataSourceImpl(usersDataManager)

    fun provideProjectRemoteDataSource(
        projectDataManager: ProjectDataManager
    ): RemoteDataSourceImpl = RemoteDataSourceImpl(projectDataManager)
}












