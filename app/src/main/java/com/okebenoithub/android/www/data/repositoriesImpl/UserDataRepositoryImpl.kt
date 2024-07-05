package com.okebenoithub.android.www.data.repositoriesImpl

import com.okebenoithub.android.www.data.datasources.user.LocalDataSource
import com.okebenoithub.android.www.data.datasources.user.RemoteDataSource
import com.okebenoithub.android.www.data.sqlite.user.DbUserModel
import com.okebenoithub.android.www.domain.repositories.UserDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * UsersDataRepositoryImpl is an implementation of UsersDataRepository.
 * This class is responsible for handling data operations related to users.
 * @param localDataSource: LocalDataSource
 * @param remoteDataSource: RemoteDataSource
 */
class UserDataRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): UserDataRepository {
    override suspend fun saveUser(dbUserModel: DbUserModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(
        userId: String,
        addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userId: String): DbUserModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(
        userId: String,
        getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<DbUserModel>? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(dbUserModel: DbUserModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        userId: String,
        updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(
        userId: String,
        deleteDocDataFromCollectionProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(dbUserModel: DbUserModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllUsers(): Boolean {
        TODO("Not yet implemented")
    }
}