package com.okebenoithub.android.www.data.repositoriesImpl

import com.okebenoithub.android.www.data.datasources.project.LocalDataSource
import com.okebenoithub.android.www.data.datasources.project.RemoteDataSource
import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.domain.repositories.ProjectDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

class ProjectDataRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): ProjectDataRepository {
    override suspend fun saveProject(dbProjectModel: DbProjectModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveProject(
        projectId: String,
        addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getProject(userId: String): DbProjectModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getProject(
        userId: String,
        getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProjects(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProjects(): List<DbProjectModel>? {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(dbProjectModel: DbProjectModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(
        projectId: String,
        updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(
        projectId: String,
        deleteDocDataFromCollectionProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(dbProjectModel: DbProjectModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllProjects(): Boolean {
        TODO("Not yet implemented")
    }
}