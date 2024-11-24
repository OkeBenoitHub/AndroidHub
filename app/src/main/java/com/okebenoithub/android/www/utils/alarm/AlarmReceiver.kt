package com.okebenoithub.android.www.utils.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.okebenoithub.android.www.utils.MainUtil

/**
 * Alarm Receiver
 * This class is used to receive the alarm event and perform the necessary actions.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Handle the alarm event here, like sending a notification or starting an activity
        MainUtil().showToastMessage(context, "Alarm triggered!")
        // start an activity or service
    }

    private fun startActivity(context: Context, intent: Intent) {
        // start an activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun startService(context: Context, intent: Intent) {
        // Start a service to perform background tasks
        context.startService(intent)
    }
}