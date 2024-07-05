package com.okebenoithub.android.www.utils

import android.content.Context
import com.github.thunder413.datetimeutils.DateTimeUtils
import com.okebenoithub.android.www.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * DateTimeUtil :: contain every recurring task dealing with Date and Time
 */
class DateTimeUtil @Inject constructor() {
    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS
    }
    /**
     * Set default time zone UTC
     */
    private fun setDefaultTimeZone() {
        DateTimeUtils.setTimeZone(TimeZone.getDefault().displayName)
    }

    fun getTodayDate(): String {
        setDefaultTimeZone()
        //val sdf = SimpleDateFormat("EEEE, MMMM dd yyyy hh:mm a",Locale.getDefault())
        val sdf = SimpleDateFormat("MMMM dd yyyy hh:mm a",Locale.getDefault())
        return sdf.format(Date())
    }

    fun getDateFromTimestampInSeconds(timestamp: Long): String {
        setDefaultTimeZone()
        val sdf = SimpleDateFormat("MMMM dd hh:mm a",Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    fun getDateFromTimestampInMilliseconds(timestamp: Long): String {
        setDefaultTimeZone()
        val sdf = SimpleDateFormat("MMMM dd yyyy hh:mm a",Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getCurrentYear(): String {
        setDefaultTimeZone()
        val sdf = SimpleDateFormat("yyyy",Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Get time ago based on timestamp from past
     * @param timestamp :: timestamp
     * @return :: explicit time ago
     */
    fun getTimeAgo(timestamp: Long, context: Context): String? {
        setDefaultTimeZone()
        var time = timestamp
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            context.resources.getString(R.string.just_now_ago_text)
        } else if (diff < 2 * MINUTE_MILLIS) {
            context.resources.getString(R.string.minute_ago_text)
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " " + context.resources
                .getString(R.string.minutes_ago_text)
        } else if (diff < 90 * MINUTE_MILLIS) {
            context.resources.getString(R.string.hour_ago_text)
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " " + context.resources
                .getString(R.string.hours_ago_text)
        } else if (diff < 48 * HOUR_MILLIS) {
            context.resources.getString(R.string.yesterday_text)
        } else {
            (diff / DAY_MILLIS).toString() + " " + context.resources
                .getString(R.string.days_ago_text)
        }
    }
}