package com.okebenoithub.android.www.data.datasourcesImpl.project

import com.okebenoithub.android.www.data.datasources.project.RemoteDataSource
import com.okebenoithub.android.www.data.firebase.ProjectDataManager
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

class RemoteDataSourceImpl(private val projectDataManager: ProjectDataManager): RemoteDataSource {
    override suspend fun saveProject(
        projectId: String,
        addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(
        projectId: String,
        deleteDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(
        projectId: String,
        updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getProject(
        projectId: String,
        getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProjects(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback) {
        TODO("Not yet implemented")
    }
}