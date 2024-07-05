package com.okebenoithub.android.www.utils.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import javax.inject.Inject

/**
 * Firebase Storage :: contain every recurring task dealing with Firebase file storage
 */
class FileStorageUtil @Inject constructor() {
    /**
     * Access a Cloud Firebase Storage instance
     * @return void
     */
    private fun getFirebaseStorageInstance(): FirebaseStorage {
        return Firebase.storage
    }

    // Access a Cloud Firebase Storage reference
    private fun getFirebaseStorageRef(): StorageReference {
        return getFirebaseStorageInstance().reference
    }

    // interface that will check for generated doc data add process status
    interface UploadLocalFileToStorageCallback {
        fun onUploadLocalFileToStorageProcessStatus(isSuccessful: Boolean, errorMessage: String?, downloadUri: Uri?, storageException: StorageException?)
    }

    /**
     * Upload local file to Cloud Storage
     * @param context
     * @param localFilePath the local file path
     * @param cloudFileDir the cloud file directory
     * @param fileContentType the file content type
     * @param uploadLocalFileToStorageCallback the callback
     */
    fun uploadLocalFileToStorage(
        context: Context,
        localFilePath: String?,
        cloudFileDir: String,
        fileContentType: String,
        uploadLocalFileToStorageCallback: UploadLocalFileToStorageCallback
    ) {
        if (localFilePath != null) {
            val file = Uri.fromFile(File(localFilePath))
            val fileStorageRef =
                getFirebaseStorageRef().child("$cloudFileDir/${file.lastPathSegment}")
            // Create file metadata including the content type
            val metadata = storageMetadata {
                contentType = fileContentType //"image/jpg"
            }
            fileStorageRef.putFile(file, metadata)
                .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        uploadLocalFileToStorageCallback.onUploadLocalFileToStorageProcessStatus(
                            false, it.localizedMessage, null, it as StorageException
                        )
                        throw it
                    }
                }
                fileStorageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    uploadLocalFileToStorageCallback.onUploadLocalFileToStorageProcessStatus(
                        true,
                        null,downloadUri, null
                    )
                } else {
                    // Handle failures
                    uploadLocalFileToStorageCallback.onUploadLocalFileToStorageProcessStatus(
                        false,
                        task.exception?.localizedMessage, null, task.exception as StorageException
                    )
                }
            }
        }
    }

    // interface that will check for deletion process status
    interface DeleteFileRefFromStorageCallback {
        fun onCloudFileRefDeleted(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Delete file reference from Cloud Storage
     * @param context
     * @param cloudFileRefPath the cloud file reference path
     * @param deleteFileRefFromStorageCallback the callback
     */
    fun deleteFileRefFromStorage(context: Context,cloudFileRefPath: String, deleteFileRefFromStorageCallback: DeleteFileRefFromStorageCallback) {
        // Create a reference to the file to delete
        val cloudFileRef = getFirebaseStorageRef().child(cloudFileRefPath)

        // Delete the file reference
        cloudFileRef.delete().addOnSuccessListener {
            // File deleted successfully
            deleteFileRefFromStorageCallback.onCloudFileRefDeleted(true,null)
        }.addOnFailureListener { errorMessage ->
            // Uh-oh, an error occurred!
            deleteFileRefFromStorageCallback.onCloudFileRefDeleted(false,errorMessage.toString())
        }
    }
}