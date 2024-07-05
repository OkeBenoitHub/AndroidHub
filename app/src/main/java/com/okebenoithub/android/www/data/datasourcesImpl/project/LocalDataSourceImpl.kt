package com.okebenoithub.android.www.data.datasourcesImpl.project

import com.okebenoithub.android.www.data.datasources.project.LocalDataSource
import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel
import com.okebenoithub.android.www.data.sqlite.project.ProjectsDAO

/**
 * Implementation of the [LocalDataSource] interface
 * @param projectsDAO the dao for accessing project data
 */
class LocalDataSourceImpl(private val projectsDAO: ProjectsDAO): LocalDataSource {
    override suspend fun saveProject(dbProjectModel: DbProjectModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(dbProjectModel: DbProjectModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllProjects(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(dbProjectModel: DbProjectModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getProject(projectId: String): DbProjectModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProjects(): List<DbProjectModel>? {
        TODO("Not yet implemented")
    }
}