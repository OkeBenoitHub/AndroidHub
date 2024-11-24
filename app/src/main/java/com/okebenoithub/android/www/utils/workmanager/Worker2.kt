package com.okebenoithub.android.www.utils.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class Worker2(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Retrieve input data
        val stringData: String? = inputData.getString("key_string")
        val intData: Int = inputData.getInt("key_int", -1) // default value -1 if key doesn't exist
        val booleanData: Boolean = inputData.getBoolean("key_boolean", false)
        val floatData: Float = inputData.getFloat("key_float", 0f)
        val doubleData: Double = inputData.getDouble("key_double", 0.0)
        val userId = inputData.getString("user_id") ?: "default_user_id"
        // Use the data as needed in your work
        // ...
        return Result.success()
    }
}