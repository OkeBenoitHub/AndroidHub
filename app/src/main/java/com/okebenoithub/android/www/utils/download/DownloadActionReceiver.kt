package com.okebenoithub.android.www.utils.download

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * This class is used to handle actions related to downloading files.
 */
class DownloadActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
        val downloadId = intent.getIntExtra("downloadId", 0)
        val kDownloaderUtil = KDownloaderUtil(context)

        when (intent.action) {
            "CANCEL_DOWNLOAD" -> {
                kDownloaderUtil.cancelDownload(downloadId)
                kDownloaderUtil.updateNotification(context, notificationId, "Download Canceled", "The download has been canceled.")
            }
            "PAUSE_DOWNLOAD" -> {
                kDownloaderUtil.pauseDownload(downloadId)
                kDownloaderUtil.updateNotification(context, notificationId, "Download Paused", "The download has been paused.")
            }
        }
    }
}