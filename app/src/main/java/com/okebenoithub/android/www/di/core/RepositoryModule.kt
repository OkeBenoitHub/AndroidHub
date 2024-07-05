package com.okebenoithub.android.www.di.core
import com.okebenoithub.android.www.data.repositoriesImpl.ProjectDataRepositoryImpl
import com.okebenoithub.android.www.data.repositoriesImpl.UserDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RepositoryModule
 * This class will provide the repository module
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserDataRepository(
        localDataSource: com.okebenoithub.android.www.data.datasources.user.LocalDataSource,
        remoteDataSource: com.okebenoithub.android.www.data.datasources.user.RemoteDataSource
    ) = UserDataRepositoryImpl(localDataSource, remoteDataSource)

    fun provideProjectDataRepository(
        localDataSource: com.okebenoithub.android.www.data.datasources.project.LocalDataSource,
        remoteDataSource: com.okebenoithub.android.www.data.datasources.project.RemoteDataSource
    ) = ProjectDataRepositoryImpl(localDataSource, remoteDataSource)
}














