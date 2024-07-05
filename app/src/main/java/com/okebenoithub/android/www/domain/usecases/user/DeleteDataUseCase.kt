package com.okebenoithub.android.www.domain.usecases.user
import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.domain.repositories.UserDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Delete user data use case
 * This use case deletes user data from the local database
 * @param userDataRepository the user data repository
 */
class DeleteDataUseCase(private val userDataRepository: UserDataRepository) {

    /**
     * Executes the use case
     * @param userId the user id
     * @param deleteDocDataFromCollectionProcessCallback the delete doc data from collection process callback
     */
    suspend fun execute(userId: String, deleteDocDataFromCollectionProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback) {
        userDataRepository.deleteUser(userId, deleteDocDataFromCollectionProcessCallback)
    }

    /**
     * Executes the use case
     * @param dbUserModel the user model
     */
    suspend fun execute(dbUserModel: DbUserModel) = userDataRepository.deleteUser(dbUserModel)

    // Delete all users
    suspend fun clearAllUsers() = userDataRepository.deleteAllUsers()
}