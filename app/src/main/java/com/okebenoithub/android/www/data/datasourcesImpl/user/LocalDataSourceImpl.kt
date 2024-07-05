package com.okebenoithub.android.www.data.datasourcesImpl.user

import com.okebenoithub.android.www.data.datasources.user.LocalDataSource
import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.data.sqlite.user.UsersDAO

/**
 * LocalDataSourceImpl class
 * This class implements the LocalDataSource interface.
 * @param usersDAO: UsersDAO
 */
class LocalDataSourceImpl(private val usersDAO: UsersDAO): LocalDataSource {

    /**
     * Save user to database
     * @param dbUserModel: DbUserModel
     */
    override suspend fun saveUser(dbUserModel: DbUserModel): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Delete user from database
     * @param dbUserModel: DbUserModel
     */
    override suspend fun deleteUser(dbUserModel: DbUserModel): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Delete all users from database
     */
    override suspend fun deleteAllUsers(): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Update user in database
     * @param dbUserModel: DbUserModel
     */
    override suspend fun updateUser(dbUserModel: DbUserModel): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Get user from database
     * @param userId: String
     * @return DbUserModel?
     */
    override suspend fun getUser(userId: String): DbUserModel? {
        TODO("Not yet implemented")
    }

    /**
     * Get all users from database
     * @return List<DbUserModel>?
     */
    override suspend fun getAllUsers(): List<DbUserModel>? {
        TODO("Not yet implemented")
    }
}