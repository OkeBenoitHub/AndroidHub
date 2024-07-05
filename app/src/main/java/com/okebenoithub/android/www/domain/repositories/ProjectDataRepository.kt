package com.okebenoithub.android.www.domain.repositories

import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

// ProjectDataRepository interface
interface ProjectDataRepository {
    // Save project data
    suspend fun saveProject(dbProjectModel: DbProjectModel): Boolean
    suspend fun saveProject(projectId: String, addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback)

    // Get project data
    suspend fun getProject(userId: String): DbProjectModel?
    suspend fun getProject(userId: String, getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback)
    suspend fun getAllProjects(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback)
    suspend fun getAllProjects(): List<DbProjectModel>?

    // Update project data
    suspend fun updateProject(dbProjectModel: DbProjectModel): Boolean
    suspend fun updateProject(projectId: String, updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback)

    // Delete project data
    suspend fun deleteProject(projectId: String, deleteDocDataFromCollectionProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback)
    suspend fun deleteProject(dbProjectModel: DbProjectModel): Boolean
    suspend fun deleteAllProjects(): Boolean
}