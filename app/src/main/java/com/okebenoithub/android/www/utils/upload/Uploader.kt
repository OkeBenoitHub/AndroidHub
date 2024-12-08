package com.okebenoithub.android.www.utils.upload

import android.content.Context
import java.io.File

/**
 * Uploader :: contain every recurring task dealing with file uploading
 */
class Uploader(
    private val context: Context,
    serverUrl: String,
    authToken: String? = null,
    filesDir: File = File(context.filesDir, "uploads")
) {

    // Example files
    val file1 = File(filesDir, "file1.txt").apply { writeText("File 1 content") }
    val file2 = File(filesDir, "file2.txt").apply { writeText("File 2 content") }

    private val uploader = FileUploadUtil(serverUrl, authToken)

    // Upload single file
    fun uploadSingleFile() {
        // Single file upload with progress
        uploader.uploadFile(
            file = file1,
            fileParamName = "file",
            additionalParams = mapOf("userId" to "12345"),
            onProgress = { progress ->
                println("Upload progress: $progress%")
            },
            onComplete = { success, message ->
                if (success) {
                    println("File uploaded successfully: $message")
                } else {
                    println("File upload failed: $message")
                }
            }
        )
    }

    // Upload multiple files
    fun uploadMultipleFiles() {
        // Multiple files upload
        uploader.uploadMultipleFiles(
            files = listOf(file1, file2),
            fileParamName = "files",
            additionalParams = mapOf("userId" to "12345")
        ) { success, message ->
            if (success) {
                println("All files uploaded successfully: $message")
            } else {
                println("File upload failed: $message")
            }
        }
    }
}