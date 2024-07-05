package com.okebenoithub.android.www.domain.usecases.project
import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.domain.repositories.ProjectDataRepository
import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

/**
 * Delete project data use case
 * This use case deletes project data from room or firebase
 * @param projectDataRepository the project data repository
 */
class DeleteDataUseCase(private val projectDataRepository: ProjectDataRepository) {

    /**
     * Executes the use case
     * @param projectId the project id
     * @param deleteDocDataFromCollectionProcessCallback the delete doc data from collection process callback
     */
    suspend fun execute(projectId: String, deleteDocDataFromCollectionProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback) {
        projectDataRepository.deleteProject(projectId, deleteDocDataFromCollectionProcessCallback)
    }

    /**
     * Executes the use case
     * @param dbProjectModel the db project model
     */
    suspend fun execute(dbProjectModel: DbProjectModel) = projectDataRepository.deleteProject(dbProjectModel)

    // Delete all projects
    suspend fun clearAllProjects() = projectDataRepository.deleteAllProjects()
}