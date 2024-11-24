package com.okebenoithub.android.www.utils.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

object AlarmManagerUtil {
    private const val ALARM_REQUEST_CODE = 1001

    /**
     * Get a PendingIntent for the alarm.
     * @param context The application context.
     * @param requestCode The request code for the PendingIntent.
     * @return The PendingIntent for the alarm.
     ***/
    private fun getPendingIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Set a one-time alarm.
     * @param context The application context.
     * @param triggerAtMillis The time in milliseconds to trigger the alarm.
     */
    fun setOneTimeAlarm(context: Context, triggerAtMillis: Long, requestCode: Int = ALARM_REQUEST_CODE) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Get a PendingIntent for the alarm
        val pendingIntent = getPendingIntent(context, requestCode)

        // Set the alarm to trigger at the specified time
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    /**
     * Set a repeating alarm.
     * @param context The application context.
     * @param intervalMillis The interval in milliseconds between alarm triggers.
     */
    fun setRepeatingAlarm(context: Context, intervalMillis: Long, requestCode: Int = ALARM_REQUEST_CODE) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Get a PendingIntent for the alarm
        val pendingIntent = getPendingIntent(context, requestCode)

        // Set the alarm to trigger at the specified interval
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + intervalMillis,
            intervalMillis,
            pendingIntent
        )
    }

    /**
     * Set an inexact repeating alarm.
     * @param context The application context.
     * @param intervalMillis The interval in milliseconds between alarm triggers.
     */
    fun setInexactRepeatingAlarm(context: Context, intervalMillis: Long, requestCode: Int = ALARM_REQUEST_CODE) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context, requestCode)

        // set the alarm to trigger inexactly every 15 minutes
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + intervalMillis,
            intervalMillis,
            pendingIntent
        )
    }

    /**
     * Check if an alarm is set.
     * @param context The application context.
     * @param requestCode The request code for the PendingIntent.
     */
    fun isAlarmSet(context: Context, requestCode: Int = ALARM_REQUEST_CODE): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarms = alarmManager.nextAlarmClock
        return alarms != null
    }

    fun updateAlarm(context: Context, triggerAtMillis: Long, intervalMillis: Long? = null, requestCode: Int = ALARM_REQUEST_CODE) {
        cancelAlarm(context, requestCode)
        // Set the new alarm
        if (intervalMillis != null) {
            setRepeatingAlarm(context, intervalMillis, requestCode)
        } else {
            setOneTimeAlarm(context, triggerAtMillis, requestCode)
        }
    }

    /**
     * Cancel an alarm.
     * @param context The application context.
     * @param requestCode The request code for the PendingIntent.
     */
    fun cancelAlarm(context: Context, requestCode: Int = ALARM_REQUEST_CODE) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Get a PendingIntent for the alarm
        val pendingIntent = getPendingIntent(context, requestCode)

        // Cancel the alarm
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Set an alarm with an action.
     * @param context The application context.
     * @param triggerAtMillis The time in milliseconds to trigger the alarm.
     * @param action The action to perform when the alarm is triggered.
     */
    fun setAlarmWithAction(context: Context, triggerAtMillis: Long, action: String, requestCode: Int = ALARM_REQUEST_CODE) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm to trigger at the specified time
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }
}