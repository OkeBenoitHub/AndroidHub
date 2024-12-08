package com.okebenoithub.android.www.utils.upload

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.buffer
import okio.source
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * File upload util :: contain every recurring task dealing with file uploading
 */
class FileUploadUtil(
    private val serverUrl: String,
    private val authToken: String? = null, // Optional authentication token
    private val maxRetries: Int = 3 // Maximum retries for failed uploads
) {

    // Create an OkHttpClient with logging interceptor
    private val client: OkHttpClient

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // init client with logging interceptor
        client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Uploads a single file with progress tracking.
     * @param file The file to upload.
     * @param fileParamName Name of the file parameter in the request.
     * @param additionalParams Additional parameters to include in the request.
     * @param onProgress Callback for progress updates.
     * @param onComplete Callback for the upload completion.
     * @return The response body as a string.
     */
    fun uploadFile(
        file: File,
        fileParamName: String,
        additionalParams: Map<String, String> = emptyMap(),
        onProgress: (progress: Int) -> Unit = {},
        onComplete: (Boolean, String) -> Unit
    ) {
        val requestBody = createMultipartRequestBodyWithProgress(file, fileParamName, additionalParams, onProgress)
        val request = buildRequest(requestBody)
        executeRequest(request, onComplete)
    }

    /**
     * Uploads multiple files simultaneously.
     * @param files List of files to upload.
     * @param fileParamName Name of the file parameter in the request.
     * @param additionalParams Additional parameters to include in the request.
     * @param onComplete Callback for the upload completion.
     * @return The response body as a string.
     */
    fun uploadMultipleFiles(
        files: List<File>,
        fileParamName: String,
        additionalParams: Map<String, String> = emptyMap(),
        onComplete: (Boolean, String) -> Unit
    ) {
        val requestBody = createMultipartRequestBody(files, fileParamName, additionalParams)
        val request = buildRequest(requestBody)
        executeRequest(request, onComplete)
    }

    /**
     * Create multipart request body with progress tracking.
     * @param file The file to upload.
     * @param fileParamName Name of the file parameter in the request.
     * @param additionalParams Additional parameters to include in the request.
     * @param onProgress Callback for progress updates.
     * @return The multipart request body.
     */
    private fun createMultipartRequestBodyWithProgress(
        file: File,
        fileParamName: String,
        additionalParams: Map<String, String>,
        onProgress: (progress: Int) -> Unit = {}
    ): RequestBody {
        val totalBytes = file.length()

        return object : RequestBody() {
            override fun contentType() = "multipart/form-data".toMediaType()

            override fun writeTo(sink: okio.BufferedSink) {
                // Buffer for the file data
                val fileSource = file.source().buffer()

                fileSource.use { source ->
                    val buffer = okio.Buffer() // This is what we'll use to stream the file
                    var bytesWritten = 0L
                    var read: Long

                    while (source.read(buffer, DEFAULT_BUFFER_SIZE.toLong()).also { read = it } != -1L) {
                        sink.write(buffer, read) // Write from the buffer to the sink
                        bytesWritten += read

                        // Update progress
                        val progress = (bytesWritten * 100 / totalBytes).toInt()
                        onProgress(progress)
                    }
                }
            }
        }
    }

    /**
     * Create multipart request body for multiple files.
     * @param files List of files to upload.
     * @param fileParamName Name of the file parameter in the request.
     * @param additionalParams Additional parameters to include in the request.
     * @return The multipart request body.
     */
    private fun createMultipartRequestBody(
        files: List<File>,
        fileParamName: String,
        additionalParams: Map<String, String>
    ): RequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        // Add files
        files.forEach { file ->
            val fileBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            builder.addFormDataPart(fileParamName, file.name, fileBody)
        }

        // Add additional params
        additionalParams.forEach { (key, value) ->
            builder.addFormDataPart(key, value)
        }

        return builder.build()
    }

    /**
     * Builds the HTTP request, including authentication if provided.
     * @param body The request body.
     * @return The built Request object.
     */
    private fun buildRequest(body: RequestBody): Request {
        val builder = Request.Builder()
            .url(serverUrl)
            .post(body)

        authToken?.let {
            builder.addHeader("Authorization", "Bearer $it")
        }

        return builder.build()
    }

    /**
     * Executes the request with retries.
     * @param request The HTTP request to execute.
     * @param onComplete Callback for the request completion.
     */
    private fun executeRequest(
        request: Request,
        onComplete: (Boolean, String) -> Unit
    ) {
        var retryCount = 0

        fun attempt() {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (retryCount < maxRetries) {
                        retryCount++
                        attempt()
                    } else {
                        onComplete(false, "Failed after $maxRetries retries: ${e.message}")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (it.isSuccessful) {
                            onComplete(true, it.body?.string() ?: "Success")
                        } else {
                            onComplete(false, "Error: ${it.code} - ${it.message}")
                        }
                    }
                }
            })
        }
        attempt()
    }
}