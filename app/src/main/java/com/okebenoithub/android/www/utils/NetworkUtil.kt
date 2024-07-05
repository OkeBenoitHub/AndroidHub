package com.okebenoithub.android.www.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.okebenoithub.android.www.components.NETWORK_STATUS_PREF_KEY
import com.okebenoithub.android.www.components.NO_NETWORK
import javax.inject.Inject

// Connection type
enum class ConnectionType {
    Wifi, Cellular
}

/*
 * Network utility class
 * This class is used to check for internet connection
 */
class NetworkUtil @Inject constructor(context: Context) {
    private var mContext = context

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    lateinit var result: ((isAvailable: Boolean, type: ConnectionType?) -> Unit)

    /**
     * Check for internet connection
     */
    fun checkForInternetConnection(): Boolean {
        // check for internet connection
        val networkStatusPrefValue = SharedPrefUtil().getDataStringFromSharedPreferences(mContext, NETWORK_STATUS_PREF_KEY)
        return networkStatusPrefValue != NO_NETWORK
    }

    // Register network callback
    fun register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use NetworkCallback for Android 9 and above
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager.activeNetwork == null) {
                // UNAVAILABLE
                result(false,null)
            }

            // Check when the connection changes
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)

                    // UNAVAILABLE
                    result(false, null)
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    when {
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            // WIFI
                            result(true, ConnectionType.Wifi)
                        }
                        else -> {
                            // CELLULAR
                            result(true, ConnectionType.Cellular)
                        }
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            // Use Intent Filter for Android 8 and below
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            mContext.registerReceiver(networkChangeReceiver, intentFilter)
        }
    }

    // Unregister network callback
    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            mContext.unregisterReceiver(networkChangeReceiver)
        }
    }

    @Suppress("DEPRECATION")
    // Network change receiver :: to check for internet connection availability
    private val networkChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            if (activeNetworkInfo != null) {
                // Get Type of Connection
                when (activeNetworkInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        // WIFI
                        result(true, ConnectionType.Wifi)
                    }
                    else -> {
                        // CELLULAR
                        result(true, ConnectionType.Cellular)
                    }
                }
            } else {
                // UNAVAILABLE
                result(false, null)
            }
        }
    }
}