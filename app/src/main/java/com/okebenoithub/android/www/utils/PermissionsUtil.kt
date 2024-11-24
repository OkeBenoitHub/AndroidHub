package com.okebenoithub.android.www.utils

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Permissions Util
 * This class is used to handle permissions in the app.
 */
object PermissionsUtil {

    /**
     * Request permissions.
     * @param activity The activity requesting the permissions.
     * @param permissions The list of permissions to request.
     * @param requestCode The request code to use when requesting the permissions.
     **/
    fun requestPermissions(activity: FragmentActivity, permissions: Array<String>, requestCode: Int) {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), requestCode)
        }
    }

    /**
     * Check if permissions are granted.
     * @param activity The activity requesting the permissions.
     * @param permissions The list of permissions to check.
     * @return True if all permissions are granted, false otherwise.
     **/
    fun arePermissionsGranted(activity: FragmentActivity, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}