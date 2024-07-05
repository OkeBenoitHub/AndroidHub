package com.okebenoithub.android.www.domain.usecases.user

import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.domain.repositories.UserDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Save users data use case
 * This use case saves users data to room and firestore
 * @param userDataRepository users data repository
 */
class SaveDataUseCase(private val userDataRepository: UserDataRepository) {

    /**
     * Execute use case
     * @param dbUserModel users data model
     */
    suspend fun execute(dbUserModel: DbUserModel) = userDataRepository.saveUser(dbUserModel)

    /**
     * Execute use case
     * @param userId users id
     * @param addDocDataWithSpecificIdProcessCallback add doc data with specific id process callback
     */
    suspend fun execute(
        userId: String,
        addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback
    ) = userDataRepository.saveUser(userId, addDocDataWithSpecificIdProcessCallback)
}