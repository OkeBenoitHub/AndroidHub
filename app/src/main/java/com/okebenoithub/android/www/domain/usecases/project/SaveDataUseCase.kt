package com.okebenoithub.android.www.domain.usecases.project

import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.domain.repositories.ProjectDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Save project data use case
 * This use case saves project data to local database or firebase
 * @param projectDataRepository project data repository
 */
class SaveDataUseCase(private val projectDataRepository: ProjectDataRepository) {

    /**
     * Execute use case
     * @param dbProjectModel project data model
     */
    suspend fun execute(dbProjectModel: DbProjectModel) = projectDataRepository.saveProject(dbProjectModel)

    /**
     * Execute use case
     * @param projectId project id
     * @param addDocDataWithSpecificIdProcessCallback add doc data with specific id process callback
     */
    suspend fun execute(
        projectId: String,
        addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback
    ) = projectDataRepository.saveProject(projectId, addDocDataWithSpecificIdProcessCallback)
}