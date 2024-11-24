package com.okebenoithub.android.www.utils.workmanager

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class Worker1(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    // Retrieve input data
    private val stringData: String? = inputData.getString("key_string")
    private val intData: Int = inputData.getInt("key_int", -1) // default value -1 if key doesn't exist
    private val booleanData: Boolean = inputData.getBoolean("key_boolean", false)
    private val floatData: Float = inputData.getFloat("key_float", 0f)
    private val doubleData: Double = inputData.getDouble("key_double", 0.0)
    val userId = inputData.getString("user_id") ?: "default_user_id"
    private var outputData: Data.Builder = Data.Builder()

    override fun doWork(): Result {

        // Perform work here
        // ...
        performWork1()

        // Create output data
        /*
        val outputData = Data.Builder()
            .putString("output_key_1", "value1")
            .putInt("output_key_2", 42)
            .putBoolean("output_key_3", true)
            .putStringArray("output_key_4", arrayOf("item1", "item2"))
            .build()*/
        return Result.success(outputData.build())
    }

    private fun performWork1() {
        // Perform work here
        // ...
        outputData.putString("data1", "value1")
        outputData.putInt("data2", 42)
        outputData.putBoolean("data3", true)
        outputData.putStringArray("data4", arrayOf("item1", "item2"))
    }
}