package com.okebenoithub.android.www.utils

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.okebenoithub.android.www.R

/**
 * Notification Util
 * This class is used to create and send notifications.
 */
class NotificationUtil(private val context: Context, private val activity: Activity) {
    companion object {
        private const val CHANNEL_ID = "default_channel_id"
        private const val CHANNEL_NAME = "Default Channel"
        private const val CHANNEL_DESCRIPTION = "This is the default notification channel."
    }

    init {
        createNotificationChannel()
    }

    /**
     * Creates a notification channel for Android 8.0 and above.
     * This is required for showing notifications on Android 8.0 and above.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val manager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Checks if the post notification permission is granted or not.
     * @return true if the permission is granted, false otherwise.
     */
    private fun checkPostNotificationPermission(): Boolean {
        when {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            -> {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requirePostNotificationPermission()
                return false
            } else -> return true
        }
    }

    /**
     * Requests the post notification permission from the user.
     */
    private fun requirePostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    /**
     * Shows a notification.
     * @param notificationId The ID of the notification.
     * @param notification The notification to be shown.
     */
    fun showNotification(context: Context, notificationId: Int, notification: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(context)) {
            // check if the permission is granted or not
            if (checkPostNotificationPermission()) notify(notificationId, notification.build())
        }
    }

    /**
     * Get a basic notification with the given title and content text.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     */
    fun getBasicNotification(title: String, content: String, smallIconResId: Int): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setColor(ContextCompat.getColor(context, R.color.white))
        return builder
    }

    /**
     * Get a notification with a big text style.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param bigTextContent The big text content of the notification.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     */
    fun getBigTextStyleNotification(title: String, content: String, bigTextContent: String, smallIconResId: Int): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigTextContent))
        return builder
    }

    /**
     * Get a notification with a big picture style.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param image The bitmap image to use for the big picture style.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     */
    fun getBigPictureStyleNotification(title: String, content: String, smallIconResId: Int, largeIcon: Bitmap, bigPictureStyle: NotificationCompat.Style): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setLargeIcon(largeIcon)
            .setStyle(bigPictureStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder
    }

    /**
     * Shows inbox style notification.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param lines The list of lines to use for the inbox style.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     * @param notificationId The ID of the notification.
     */
    fun showInboxStyleNotification(title: String, content: String, lines: List<String>, smallIconResId: Int, notificationId: Int) {
        val inboxStyle = NotificationCompat.InboxStyle()
        lines.forEach { inboxStyle.addLine(it) }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(inboxStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (checkPostNotificationPermission()) notify(notificationId, builder.build())
        }
    }

    /**
     * Shows progress notification.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param progress The progress value.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     * @param notificationId The ID of the notification.
     */
    fun showProgressNotification(title: String, content: String, progress: Int, smallIconResId: Int, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setProgress(100, progress, false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (checkPostNotificationPermission()) notify(notificationId, builder.build())
        }
    }

    /**
     * Shows custom notification.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param intent The intent to be launched when the notification is clicked.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     * @param notificationId The ID of the notification.
     */
    fun showCustomNotification(title: String, content: String, intent: Intent, smallIconResId: Int, notificationId: Int) {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (checkPostNotificationPermission()) notify(notificationId, builder.build())
        }
    }

    /**
     * Shows custom layout notification.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param customLayout The custom layout to use for the notification.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     * @param notificationId The ID of the notification.
     */
    fun showCustomLayoutNotification(title: String, content: String, customLayout: RemoteViews, smallIconResId: Int, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setCustomContentView(customLayout)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (checkPostNotificationPermission()) notify(notificationId, builder.build())
        }
    }

    /**
     * Shows notification with 2 action buttons.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param actionBtn1Intent The intent to be launched when the first action button is clicked.
     * @param actionBtn2Intent The intent to be launched when the second action button is clicked.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     * @param actionBtn1IconResId The resource ID of the icon for the first action button.
     * @param actionBtn2IconResId The resource ID of the icon for the second action button.
     * @param actionBtn1Text The text to be displayed on the first action button.
     * @param actionBtn2Text The text to be displayed on the second action button.
     * @param notificationId The ID of the notification.
     */
    fun showNotificationWith2ActionButtons(title: String, content: String, actionBtn1Intent: Intent, actionBtn2Intent: Intent, smallIconResId: Int, actionBtn1IconResId: Int, actionBtn2IconResId: Int, actionBtn1Text: String, actionBtn2Text: String, notificationId: Int) {
        // Define the intent for the first action button
        val actionPendingIntent1: PendingIntent = PendingIntent.getActivity(
            context, 0, actionBtn1Intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Define the intent for the second action button
        val actionPendingIntent2: PendingIntent = PendingIntent.getActivity(
            context, 0, actionBtn2Intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification with action buttons
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(actionBtn1IconResId, actionBtn1Text, actionPendingIntent1)
            .addAction(actionBtn2IconResId, actionBtn2Text, actionPendingIntent2)

        with(NotificationManagerCompat.from(context)) {
            if (checkPostNotificationPermission()) notify(notificationId, builder.build())
        }
    }

    /**
     * Shows notification with 1 action button.
     * @param title The title of the notification.
     * @param content The content text of the notification.
     * @param actionBtnIntent The intent to be launched when the action button is clicked.
     * @param smallIconResId The resource ID of the small icon to use for the notification.
     * @param actionBtnIconResId The resource ID of the icon for the action button.
     * @param actionBtnText The text to be displayed on the action button.
     * @param notificationId The ID of the notification.
     */
    fun showNotificationWith1ActionButton(title: String, content: String, actionBtnIntent: Intent, smallIconResId: Int, actionBtnIconResId: Int, actionBtnText: String, notificationId: Int) {

        // define the intent for the action button
        val actionPendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, actionBtnIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification with action buttons
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(actionBtnIconResId, actionBtnText, actionPendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (checkPostNotificationPermission()) notify(notificationId, builder.build())
        }
    }
}