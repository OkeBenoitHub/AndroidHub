package com.okebenoithub.android.www.di.core

import com.okebenoithub.android.www.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * NetworkModule
 * This module is responsible for providing dependencies related to network operations.
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    /*
    @Singleton
    @Provides
    fun provideYoutubeAPIService(): YoutubeAPIService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.YOUTUBE_BASE_URL)
            .build().create(YoutubeAPIService::class.java)
    }*/
}













