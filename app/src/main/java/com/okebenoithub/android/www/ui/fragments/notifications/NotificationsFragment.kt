package com.okebenoithub.android.www.ui.fragments.notifications

import android.graphics.BitmapFactory
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.databinding.FragmentNotificationsBinding
import com.okebenoithub.android.www.utils.NotificationUtil

class NotificationsFragment : Fragment() {

    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationUtil: NotificationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationUtil = NotificationUtil(requireContext(), requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Show basic notification button click listener
        binding.basic.setOnClickListener {
            val title = "Basic Notification"
            val content = "This is a basic notification."
            val smallIconResId = R.drawable.ic_notification_white
            val notification = notificationUtil.getBasicNotification(title, content, smallIconResId)
            notification.setAutoCancel(false) // notification will not be automatically dismissed
            notification.setOngoing(true) // notification will not be swiped away
            notification.setOnlyAlertOnce(true) // notification will not be shown again
            //notification.setVibrate(longArrayOf(0)) // notification will not vibrate
            // set notification to vibrate for 5 seconds
            notification.setVibrate(longArrayOf(0, 5000))
            notification.setSound(null) // notification will not have a sound or Uri.parse("android.resource://com.okebenoithub.android.www/" + R.raw.notification_sound)
            notificationUtil.showNotification(requireContext(), 1,notification)
        }

        // Show big text style notification button click listener
        binding.bigTextStyle.setOnClickListener {
            val title = "Big Text Style Notification"
            val content = "This is a big text style notification."
            val bigTextContent = "This is a big text style notification. This is a big text style notification. This is a big text style notification."
            val smallIconResId = R.drawable.ic_notification_white
            val notification = notificationUtil.getBigTextStyleNotification(title, content, bigTextContent, smallIconResId)
            notificationUtil.showNotification(requireContext(), 2, notification)
        }

        // Show big picture style notification button click listener
        binding.bigPictureStyle.setOnClickListener {
            val title = "Big Picture Style Notification"
            val content = "This is a big picture style notification."

            // Load the large icon and the big picture
            val largeIcon = BitmapFactory.decodeResource(requireContext().resources, R.drawable.icon_128)
            val bigPicture = BitmapFactory.decodeResource(requireContext().resources, R.drawable.android_icon)

            // Create a BigPictureStyle
            val bigPictureStyle = NotificationCompat.BigPictureStyle()
                .bigPicture(bigPicture)
                .setBigContentTitle("Big Picture Notification Title")
                .setSummaryText("This is the summary text for the big picture notification.")

            val smallIconResId = R.drawable.ic_notification_white
            val notification = notificationUtil.getBigPictureStyleNotification(title, content, smallIconResId, largeIcon, bigPictureStyle)
            notificationUtil.showNotification(requireContext(), 3, notification)
        }

        return binding.root
    }
}