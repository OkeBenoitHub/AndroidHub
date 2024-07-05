package com.okebenoithub.android.www.data.sqlite

import androidx.room.Database
import androidx.room.RoomDatabase
import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.data.sqlite.project.ProjectsDAO
import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.data.sqlite.user.UsersDAO

/**
 * Local database
 * This class is used for local database operations via Room
 */
@Database(
    // list of entities :: add more entities here
    entities = [DbUserModel::class, DbProjectModel::class],
    version = 1, // version of database
    exportSchema = false // do not export schema
)
abstract  class LocalDatabase : RoomDatabase() {
    /**
     * Add more Data Access Objects here
     */
    abstract fun getUsersDAO(): UsersDAO // users DAO
    abstract fun getProjectsDAO(): ProjectsDAO // projects DAO
}