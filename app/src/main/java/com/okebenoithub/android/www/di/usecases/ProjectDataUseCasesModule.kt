package com.okebenoithub.android.www.di.usecases

import com.okebenoithub.android.www.domain.repositories.ProjectDataRepository
import com.okebenoithub.android.www.domain.usecases.project.DeleteDataUseCase
import com.okebenoithub.android.www.domain.usecases.project.GetDataUseCase
import com.okebenoithub.android.www.domain.usecases.project.SaveDataUseCase
import com.okebenoithub.android.www.domain.usecases.project.UpdateDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProjectDataUseCasesModule {
    @Singleton
    @Provides
    fun provideGetDataUseCase(projectDataRepository: ProjectDataRepository): GetDataUseCase = GetDataUseCase(projectDataRepository)

    @Singleton
    @Provides
    fun provideDeleteDataUseCase(projectDataRepository: ProjectDataRepository): DeleteDataUseCase = DeleteDataUseCase(projectDataRepository)

    @Singleton
    @Provides
    fun provideSaveDataUseCase(projectDataRepository: ProjectDataRepository): SaveDataUseCase = SaveDataUseCase(projectDataRepository)

    @Singleton
    @Provides
    fun provideUpdateDataUseCase(projectDataRepository: ProjectDataRepository): UpdateDataUseCase = UpdateDataUseCase(projectDataRepository)
}