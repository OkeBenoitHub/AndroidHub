package com.okebenoithub.android.www.data.datasources.project

import com.okebenoithub.android.www.data.sqlite.project.DbProjectModel

interface LocalDataSource {
    // save project data to local database
    suspend fun saveProject(dbProjectModel: DbProjectModel): Boolean

    // delete project data from local database
    suspend fun deleteUser(dbProjectModel: DbProjectModel): Boolean

    // delete all projects data from local database
    suspend fun deleteAllProjects(): Boolean

    // update project data in local database
    suspend fun updateProject(dbProjectModel: DbProjectModel): Boolean

    // get project data from local database
    suspend fun getProject(projectId: String): DbProjectModel?

    // get all projects data from local database
    suspend fun getAllProjects(): List<DbProjectModel>?
}