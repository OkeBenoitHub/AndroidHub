package com.okebenoithub.android.www.data.datasources.user

import com.okebenoithub.android.www.data.sqlite.user.DbUserModel

interface LocalDataSource {
    // save user data to local database
    suspend fun saveUser(dbUserModel: DbUserModel): Boolean

    // delete user data from local database
    suspend fun deleteUser(dbUserModel: DbUserModel): Boolean

    // delete all user data from local database
    suspend fun deleteAllUsers(): Boolean

    // update user data in local database
    suspend fun updateUser(dbUserModel: DbUserModel): Boolean

    // get user data from local database
    suspend fun getUser(userId: String): DbUserModel?

    // get all user data from local database
    suspend fun getAllUsers(): List<DbUserModel>?
}