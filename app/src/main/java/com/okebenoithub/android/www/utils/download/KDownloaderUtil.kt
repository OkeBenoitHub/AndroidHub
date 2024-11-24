package com.okebenoithub.android.www.utils.download

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import com.kdownloader.KDownloader
import java.io.File

class KDownloaderUtil(private val context: Context) {

    private val kDownloader = KDownloader.create(context)
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    interface DownloadCallback {
        fun onDownloadStarted()
        fun onDownloadProgress(progress: Int, downloadKey: String)
        fun onDownloadComplete(downloadKey: String)
        fun onDownloadPaused(downloadKey: String)
        fun onDownloadError(downloadKey: String)
    }

    init {
        createNotificationChannel() // Create the notification channel for Android 8.0 and above
    }

    /**
     * Download a file from the given URL to the specified destination path.
     * @param url The URL of the file to download.
     * @param dirName The name of the directory where the downloaded file should be saved.
     * @param fileName The name of the downloaded file.
     * @param downloadCallback The callback to be invoked when the download process starts, progresses, completes, or fails.
     */
    fun downloadFile(url: String, dirName: String, fileName: String, isSavedToSdCard: Boolean = false, downloadKey: String, downloadCallback: DownloadCallback): Int {
        val dirNamePath = when {
            isSavedToSdCard -> getSdCardPath(dirName, fileName).toString()
            else -> getInternalStoragePath(dirName, fileName).toString()
        }
        val request = kDownloader
            .newRequestBuilder(url, dirNamePath, fileName)
            //.tag("TAG")
            .build()

        // Using all of these lambdas is not mandatory. for example - you can only use onStart or onProgress also
        return kDownloader.enqueue(request,
            onStart = {
                downloadCallback.onDownloadStarted()
            },
            onProgress = {
                downloadCallback.onDownloadProgress(it, downloadKey)
            },
            onCompleted = {
                downloadCallback.onDownloadComplete(downloadKey)
            },
            onError = {
                downloadCallback.onDownloadError(downloadKey)
            },
            onPause = {
                downloadCallback.onDownloadPaused(downloadKey)
            }
        )
    }

    /**
     * Pause a file download by its unique ID.
     */
    fun pauseDownload(downloadId: Int) {
        kDownloader.pause(downloadId)
    }

    /**
     * Resume a file download by its unique ID.
     */
    fun resumeDownload(downloadId: Int) {
        kDownloader.resume(downloadId)
    }

    /**
     * Cancel a file download by its unique ID.
     */
    fun cancelDownload(downloadId: Int) {
        kDownloader.cancel(downloadId)
    }

    /**
     * Cancel all active downloads.
     */
    fun cancelAllDownloads() {
        kDownloader.cancelAll()
    }

    /**
     * Get the path of the internal storage directory.
     */
    private fun getInternalStoragePath(dirName: String, fileName: String): String? {
        return try {
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + dirName
            // val file = File(downloadDir, fileName)
            downloadDir
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get the path of the SD card directory.
     */
    private fun getSdCardPath(dirName: String, fileName: String): String? {
        return if (isSdCardAvailable()) {
            try {
                val sdCardDir = File(Environment.getExternalStorageDirectory(), dirName)
                if (!sdCardDir.exists()) {
                    sdCardDir.mkdirs()
                }
                // val file = File(sdCardDir, fileName)
                sdCardDir.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    // Helper function to check if the SD card is available
    private fun isSdCardAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * Show a notification with progress information.
     * @param progressPercent The progress percentage.
     */

    fun showProgressNotification(title: String, content: String, downloadId: Int, notificationId: Int,  progressPercent: Int) {
        val cancelIntent = Intent(context, DownloadActionReceiver::class.java).apply {
            action = "CANCEL_DOWNLOAD"
            putExtra("downloadId", downloadId)
            putExtra("notificationId", notificationId)
        }
        val cancelPendingIntent = PendingIntent.getBroadcast(context, notificationId, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(context, DownloadActionReceiver::class.java).apply {
            action = "PAUSE_DOWNLOAD"
            putExtra("downloadId", downloadId)
            putExtra("notificationId", notificationId)
        }
        val pausePendingIntent = PendingIntent.getBroadcast(context, notificationId, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, "download_channel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, progressPercent, false)
            .addAction(android.R.drawable.ic_delete, "Cancel", cancelPendingIntent)
            .addAction(android.R.drawable.ic_media_pause, "Pause", pausePendingIntent)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Show a notification when the download is complete.
     * @param title The title of the notification.
     * @param content The content of the notification.
     */
    fun showCompleteNotification(notificationId: Int, title: String, content: String) {
        val notification = NotificationCompat.Builder(context, "download_channel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setProgress(0, 0, false)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Show a notification when the download fails.
     * @param title The title of the notification.
     * @param content The content of the notification.
     */
    fun showErrorNotification(notificationId: Int, title: String, content: String) {
        val notification = NotificationCompat.Builder(context, "download_channel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setProgress(0, 0, false)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Update a notification with progress information.
     * @param context The context of the application.
     * @param notificationId The ID of the notification to be updated.
     * @param title The title of the notification.
     * @param content The content of the notification.
     */
    fun updateNotification(context: Context, notificationId: Int, title: String, content: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "download_channel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Create a notification channel for Android 8.0 and above.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("download_channel", "File Downloads", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
    }
}