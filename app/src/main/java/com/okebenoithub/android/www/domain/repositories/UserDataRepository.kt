package com.okebenoithub.android.www.domain.repositories

import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

// Repository interface
interface UserDataRepository {
    // Save user data
    suspend fun saveUser(dbUserModel: DbUserModel): Boolean
    suspend fun saveUser(userId: String, addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback)

    // Get user data
    suspend fun getUser(userId: String): DbUserModel?
    suspend fun getUser(userId: String, getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback)
    suspend fun getAllUsers(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback)
    suspend fun getAllUsers(): List<DbUserModel>?

    // Update user data
    suspend fun updateUser(dbUserModel: DbUserModel): Boolean
    suspend fun updateUser(userId: String, updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback)

    // Delete user data
    suspend fun deleteUser(userId: String, deleteDocDataFromCollectionProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback)
    suspend fun deleteUser(dbUserModel: DbUserModel): Boolean
    suspend fun deleteAllUsers(): Boolean
}