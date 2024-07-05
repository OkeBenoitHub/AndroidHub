package com.okebenoithub.android.www.domain.usecases.user

import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.domain.repositories.UserDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Get users data use case
 * This use case gets users data from repository
 * @param userDataRepository users data repository
 */
class GetDataUseCase(private val userDataRepository: UserDataRepository) {

    /**
     * Execute use case
     * @param userId user id
     * @return user model
     */
    suspend fun execute(userId: String): DbUserModel? = userDataRepository.getUser(userId)

    /**
     * Execute use case
     * @param userId user id
     * @param getDocDataWithSpecificIdProcessCallback get doc data with specific id process callback
     */
    suspend fun execute(userId: String, getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback) {
        userDataRepository.getUser(userId, getDocDataWithSpecificIdProcessCallback)
    }

    /**
     * Execute use case
     * @param getAllDocsDataFromCollectionProcessCallback get all docs data from collection process callback
     */
    suspend fun execute(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback) {
        userDataRepository.getAllUsers(getAllDocsDataFromCollectionProcessCallback)
    }

    /**
     * Execute use case
     * @return users data list
     */
    suspend fun execute(): List<DbUserModel>? = userDataRepository.getAllUsers()
}