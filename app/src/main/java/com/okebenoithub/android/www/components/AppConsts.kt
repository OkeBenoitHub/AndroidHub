package com.okebenoithub.android.www.components

import android.content.Intent
import android.os.Build
import android.os.Parcelable

/**
 * All constants for App here
 */

/**
 * Network Properties Preferences
 */
const val NETWORK_STATUS_PREF_KEY = "network_status_pref_key"
const val NETWORK_WIFI = "network_wifi"
const val NETWORK_CELLULAR = "network_cellular"
const val NO_NETWORK = "no network"

const val APP_PERMISSIONS_READ_WRITE_COMPLETED_PREF = "app_permissions_read_write_completed_pref"
/**
 * Intent parcelable custom object
 */
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}