package com.okebenoithub.android.www.domain.usecases.user

import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.domain.repositories.UserDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Update Users data use case
 * This use case will update users data from repository
 * @param userDataRepository users data repository
 */
class UpdateDataUseCase(private val userDataRepository: UserDataRepository) {

    /**
     * Update users data from database
     * @param dbUserModel users data model
     */
    suspend fun execute(dbUserModel: DbUserModel) = userDataRepository.updateUser(dbUserModel)

    /**
     * Update users data from firestore
     * @param userId user id
     * @param updateDocFieldDataProcessCallback update doc field data process callback
     */
    suspend fun execute(userId: String, updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback) {
        userDataRepository.updateUser(userId, updateDocFieldDataProcessCallback)
    }
}