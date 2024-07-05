package com.okebenoithub.android.www.data.datasourcesImpl.user

import com.okebenoithub.android.www.data.datasources.user.RemoteDataSource
import com.okebenoithub.android.www.data.firebase.UserDataManager
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * RemoteDataSourceImpl class
 * This class is implementation of RemoteDataSource interface.
 * @param usersDataManager UsersDataManager instance.
 */
class RemoteDataSourceImpl(private val usersDataManager: UserDataManager): RemoteDataSource {

    /**
     * Save user to Firestore database
     * @param userId User id.
     * @param addDocDataWithSpecificIdProcessCallback Process callback.
     */
    override suspend fun saveUser(
        userId: String,
        addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Delete user from Firestore database
     * @param userId User id.
     * @param deleteDocDataWithSpecificIdProcessCallback Process callback.
     */
    override suspend fun deleteUser(
        userId: String,
        deleteDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Update user in Firestore database
     * @param userId User id.
     * @param updateDocFieldDataProcessCallback Process callback.
     */
    override suspend fun updateUser(
        userId: String,
        updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Get user from Firestore database
     * @param userId User id.
     * @param getDocDataWithSpecificIdProcessCallback Process callback.
     */
    override suspend fun getUser(
        userId: String,
        getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Get all users from Firestore database
     * @param getAllDocsDataFromCollectionProcessCallback Process callback.
     */
    override suspend fun getAllUsers(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback) {
        TODO("Not yet implemented")
    }
}