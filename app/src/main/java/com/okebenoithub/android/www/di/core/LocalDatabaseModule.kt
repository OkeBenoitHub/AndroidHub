package com.okebenoithub.android.www.di.core

import android.app.Application
import androidx.room.Room
import com.okebenoithub.android.www.data.sqlite.LocalDatabase
import com.okebenoithub.android.www.data.sqlite.project.ProjectsDAO
import com.okebenoithub.android.www.data.sqlite.user.UsersDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Local database module.
 * This module provides the local database instance.
 */
@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(app: Application): LocalDatabase {
        return Room.databaseBuilder(app, LocalDatabase::class.java, "local_db_file")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideProjectsDAO(localDatabase: LocalDatabase): ProjectsDAO {
        return localDatabase.getProjectsDAO()
    }

    @Singleton
    @Provides
    fun provideUsersDAO(localDatabase: LocalDatabase): UsersDAO {
        return localDatabase.getUsersDAO()
    }
}