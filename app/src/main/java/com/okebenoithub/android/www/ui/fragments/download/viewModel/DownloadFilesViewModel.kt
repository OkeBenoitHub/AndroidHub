package com.okebenoithub.android.www.ui.fragments.download.viewModel

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import com.kdownloader.internal.DownloadRequest
import com.okebenoithub.android.www.components.App
import com.okebenoithub.android.www.data.models.DownloadModel
import java.io.File

class DownloadFilesViewModel(app: Application) : AndroidViewModel(app) {
    companion object {
        private const val TAG = "DownloadFilesViewModel"
    }

    val kDownloader = (app.applicationContext as App).kDownloader
    var isLocalPermissionsGranted = false

    // list of download items
    val downloadItems = listOf(
        DownloadModel(1, "bunny.mp4", "https://res.cloudinary.com/dw8pcwcnz/video/upload/v1721825041/sample-30s_j9gxoq.mp4"),
        DownloadModel(2, "100MB.bin", "https://res.cloudinary.com/dw8pcwcnz/video/upload/v1721825038/sample-15s_ftq5my.mp4"),
        DownloadModel(3, "docu.pdf", "https://res.cloudinary.com/dw8pcwcnz/image/upload/v1718079528/uOOtwVbSr4QDjAGIifLDwpb2Pdl_poykwx.jpg"),
        DownloadModel(4, "giphy.gif", "https://res.cloudinary.com/dw8pcwcnz/image/upload/v1702718355/cld-sample.jpg"),
        DownloadModel(5, "1GB.bin", "https://res.cloudinary.com/dw8pcwcnz/video/upload/v1721825350/sample_1280x720_surfing_with_audio_crnplv.flv"),
        DownloadModel(6, "music.mp3", "https://file-examples.com/storage/fe3286c49f6458f86eb9ed5/2017/11/file_example_MP3_5MG.mp3")
    )

    fun createDownloadRequest(downloadModel: DownloadModel, dirName: String, isSavedToSdCard: Boolean): DownloadRequest {
        return if (isSavedToSdCard) {
            kDownloader.newRequestBuilder(
                downloadModel.url, getSdCardPath(dirName), downloadModel.fileName,
            ).tag(TAG + downloadModel.id).build()
        } else {
            kDownloader.newRequestBuilder(
                downloadModel.url, getInternalStoragePath(dirName).toString(), downloadModel.fileName,
            ).tag(TAG + downloadModel.id).build()
        }
    }

    /**
     * Start a download request.
     * @param request The download request to be started.
     * @param downloadRequestCallback The callback to be invoked when the download process starts, progresses, completes, or fails.
     * @return The unique ID of the started download request.
     */

    fun startDownload(request: DownloadRequest, downloadModel: DownloadModel, downloadRequestCallback: DownloadRequestCallback): Int {
        return enqueueDownload(request, downloadModel, downloadRequestCallback)
    }

    interface DownloadRequestCallback {
        fun onDownloadStarted(downloadModel: DownloadModel)
        fun onDownloadProgress(progress: Int, downloadModel: DownloadModel)
        fun onDownloadComplete(downloadModel: DownloadModel)
        fun onDownloadPaused(downloadModel: DownloadModel)
        fun onDownloadError(downloadModel: DownloadModel)
    }

    /**
     * Enqueue a download request.
     * @param request The download request to be enqueued.
     * @param downloadRequestCallback The callback to be invoked when the download process starts, progresses, completes, or fails.
     * @return The unique ID of the enqueued download request.
     */
    private fun enqueueDownload(request: DownloadRequest, downloadModel: DownloadModel, downloadRequestCallback: DownloadRequestCallback): Int {
        return kDownloader.enqueue(request,
            onStart = {
                // Handle start event
                downloadRequestCallback.onDownloadStarted(downloadModel)
            },
            onProgress = {
                // Handle progress event
                downloadRequestCallback.onDownloadProgress(it, downloadModel)
            },
            onCompleted = {
                // Handle completed event
                downloadRequestCallback.onDownloadComplete(downloadModel)
            },
            onError = {
                // Handle error event
                downloadRequestCallback.onDownloadError(downloadModel)
            },
            onPause = {
                // Handle pause event
                downloadRequestCallback.onDownloadPaused(downloadModel)
            }
        )
    }

    /**
     * Cancel a download request.
     * @param downloadId The ID of the download request to be canceled.
     */
    fun cancelDownload(downloadId: Int) {
        kDownloader.cancel(downloadId)
    }

    /**
     * Pause a download request.
     * @param downloadId The ID of the download request to be paused.
     */
    fun pauseDownload(downloadId: Int) {
        kDownloader.pause(downloadId)
    }

    /**
     * Resume a download request.
     * @param downloadId The ID of the download request to be resumed.
     */
    fun resumeDownload(downloadId: Int) {
        kDownloader.resume(downloadId)
    }

    /**
     * Get the path of the internal storage directory.
     */
    private fun getInternalStoragePath(dirName: String): String? {
        return try {
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + dirName
            downloadDir
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get the path of the SD card directory.
     */
    private fun getSdCardPath(dirName: String): String {
        return if (isSdCardAvailable()) {
            try {
                val sdCardDir = File(Environment.getExternalStorageDirectory(), dirName)
                if (!sdCardDir.exists()) {
                    sdCardDir.mkdirs()
                }
                sdCardDir.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                getInternalStoragePath("").toString()
            }
        } else {
            getInternalStoragePath(dirName).toString()
        }
    }

    // Helper function to check if the SD card is available
    private fun isSdCardAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}