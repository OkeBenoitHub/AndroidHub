package com.okebenoithub.android.www.data.datasources.user

import com.okebenoithub.android.www.utils.firebase.FirestoreDbUtil

interface RemoteDataSource {
     // save user to firestore
     suspend fun saveUser(userId: String, addDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.AddDocDataWithSpecificIdProcessCallback)

     // delete user from firestore
     suspend fun deleteUser(userId: String, deleteDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.DeleteDocDataFromCollectionProcessCallback)

     // update user in firestore
     suspend fun updateUser(userId: String, updateDocFieldDataProcessCallback: FirestoreDbUtil.UpdateDocFieldDataProcessCallback)

     // get user from firestore
     suspend fun getUser(userId: String, getDocDataWithSpecificIdProcessCallback: FirestoreDbUtil.GetDocDataWithSpecificIdProcessCallback)

     // get all users from firestore
     suspend fun getAllUsers(getAllDocsDataFromCollectionProcessCallback: FirestoreDbUtil.GetAllDocsDataFromCollectionProcessCallback)
}