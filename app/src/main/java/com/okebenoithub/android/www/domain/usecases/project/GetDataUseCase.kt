package com.okebenoithub.android.www.domain.usecases.project

import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.domain.repositories.ProjectDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Get project data use case
 * This use case gets project data from repository
 * @param projectDataRepository project data repository
 */
class GetDataUseCase(private val projectDataRepository: ProjectDataRepository) {

    /**
     * Execute use case
     * @param projectId project id
     * @return project data model
     */
    suspend fun execute(projectId: String): DbProjectModel? = projectDataRepository.getProject(projectId)

    /**
     * Execute use case
     * @param projectId project id
     * @param getDocDataWithSpecificIdProcessCallback get doc data with specific id process callback
     */
    suspend fun execute(projectId: String, getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback) {
        projectDataRepository.getProject(projectId, getDocDataWithSpecificIdProcessCallback)
    }

    /**
     * Execute use case
     * @param getAllDocsDataFromCollectionProcessCallback get all docs data from collection process callback
     */
    suspend fun execute(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback) {
        projectDataRepository.getAllProjects(getAllDocsDataFromCollectionProcessCallback)
    }

    /**
     * Execute use case
     * @return users data list
     */
    suspend fun execute(): List<DbProjectModel>? = projectDataRepository.getAllProjects()
}