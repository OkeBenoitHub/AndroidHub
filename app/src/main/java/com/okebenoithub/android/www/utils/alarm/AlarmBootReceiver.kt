package com.okebenoithub.android.www.utils.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Boot receiver to re-set all alarms when the device is rebooted.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-set all alarms here, if necessary
            AlarmManagerUtil.setOneTimeAlarm(context, triggerAtMillis = 10000)
            AlarmManagerUtil.setRepeatingAlarm(context, intervalMillis = 10000)
        }
    }
}