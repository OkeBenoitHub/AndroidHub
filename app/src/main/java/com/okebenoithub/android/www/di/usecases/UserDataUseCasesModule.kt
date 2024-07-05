package com.okebenoithub.android.www.di.usecases

import com.okebenoithub.android.www.domain.repositories.UserDataRepository
import com.okebenoithub.android.www.domain.usecases.user.DeleteDataUseCase
import com.okebenoithub.android.www.domain.usecases.user.GetDataUseCase
import com.okebenoithub.android.www.domain.usecases.user.SaveDataUseCase
import com.okebenoithub.android.www.domain.usecases.user.UpdateDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserDataUseCasesModule {

    @Singleton
    @Provides
    fun provideGetDataUseCase(userDataRepository: UserDataRepository): GetDataUseCase = GetDataUseCase(userDataRepository)

    @Singleton
    @Provides
    fun provideDeleteDataUseCase(userDataRepository: UserDataRepository): DeleteDataUseCase = DeleteDataUseCase(userDataRepository)

    @Singleton
    @Provides
    fun provideSaveDataUseCase(userDataRepository: UserDataRepository): SaveDataUseCase = SaveDataUseCase(userDataRepository)

    @Singleton
    @Provides
    fun provideUpdateDataUseCase(userDataRepository: UserDataRepository): UpdateDataUseCase = UpdateDataUseCase(userDataRepository)
}