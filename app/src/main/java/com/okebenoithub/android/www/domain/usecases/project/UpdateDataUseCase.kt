package com.okebenoithub.android.www.domain.usecases.project

import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.domain.repositories.ProjectDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Update Project data use case
 * This use case will update project data
 * @param projectDataRepository project data repository
 */
class UpdateDataUseCase(private val projectDataRepository: ProjectDataRepository) {

    /**
     * Update project data
     * @param dbProjectModel project model
     */
    suspend fun execute(dbProjectModel: DbProjectModel) = projectDataRepository.updateProject(dbProjectModel)

    /**
     * Update project data from firestore
     * @param projectId project id
     * @param updateDocFieldDataProcessCallback update doc field data process callback
     */
    suspend fun execute(projectId: String, updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback) {
        projectDataRepository.updateProject(projectId, updateDocFieldDataProcessCallback)
    }
}