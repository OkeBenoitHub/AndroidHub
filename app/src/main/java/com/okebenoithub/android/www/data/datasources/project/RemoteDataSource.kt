package com.okebenoithub.android.www.data.datasources.project

import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

interface RemoteDataSource {
     // save project to firestore
     suspend fun saveProject(projectId: String, addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback)

     // delete project from firestore
     suspend fun deleteProject(projectId: String, deleteDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback)

     // update project in firestore
     suspend fun updateProject(projectId: String, updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback)

     // get project from firestore
     suspend fun getProject(projectId: String, getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback)

     // get all projects from firestore
     suspend fun getAllProjects(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback)
}