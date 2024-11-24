package com.okebenoithub.android.www.utils.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import com.okebenoithub.android.www.utils.ObjectStorePrefUtil
import com.okebenoithub.android.www.utils.SharedPrefUtil
import java.util.UUID

/**
 * Work Manager Util
 * This class is used to schedule and manage background tasks using Work Manager.
 */
object WorkManagerUtil {

    /**
     * Schedule a one-time work request.
     *
     * @param context The application context.
     * @param workClass The worker class to execute.
     * @param inputData The input data for the work.
     * @param tag A tag to identify the work request.
     * @param constraints Optional constraints for the work.
     */
    fun scheduleOneTimeWork(
        context: Context,
        workClass: Class<out Worker>,
        inputData: Data? = null,
        tag: String? = null,
        constraints: Constraints? = null
    ): UUID {

        // Create a OneTimeWorkRequest
        val workRequestBuilder = OneTimeWorkRequest.Builder(workClass)
        inputData?.let { workRequestBuilder.setInputData(it) }
        val workRequest = workRequestBuilder.build()
        constraints?.let { workRequestBuilder.setConstraints(it) }

        // Enqueue the work request
        WorkManager.getInstance(context).enqueueUniqueWork(
            tag ?: workClass.simpleName,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

        // Return the work request ID
        return workRequest.id
    }

    /**
     * Schedule a periodic work request.
     *
     * @param context The application context.
     * @param workClass The worker class to execute.
     * @param repeatInterval The repeat interval for the periodic work in milliseconds.
     * @param inputData The input data for the work.
     * @param tag A tag to identify the work request.
     * @param constraints Optional constraints for the work.
     */
    fun schedulePeriodicWork(
        context: Context,
        workClass: Class<out Worker>,
        repeatInterval: Long,
        inputData: Data? = null,
        tag: String? = null,
        constraints: Constraints? = null
    ): UUID {

        // Create a PeriodicWorkRequest
        val workRequestBuilder = PeriodicWorkRequest.Builder(workClass, repeatInterval, java.util.concurrent.TimeUnit.MILLISECONDS)
        inputData?.let { workRequestBuilder.setInputData(it) }
        constraints?.let { workRequestBuilder.setConstraints(it) }
        val workRequest = workRequestBuilder.build()

        // Enqueue the work request
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            tag ?: workClass.simpleName,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        // Return the work request ID
        return workRequest.id
    }

    /**
     * Schedule multiple works in parallel.
     *
     * @param context The application context.
     * @param workRequests A list of triples containing worker class, input data for each work, and tags.
     */
    fun scheduleMultipleWorks(
        context: Context,
        workRequests: List<Triple<Class<out Worker>, Data?, String?>>
    ): List<UUID> {

        // Create a list of OneTimeWorkRequest objects
        val workManager = WorkManager.getInstance(context)
        val requests = workRequests.map { (workClass, inputData, tag) ->
            val workRequestBuilder = OneTimeWorkRequest.Builder(workClass)
            inputData?.let { workRequestBuilder.setInputData(it) }
            workRequestBuilder.addTag(tag ?: workClass.simpleName) // Use tag if provided, else default to class name
            workRequestBuilder.build()
        }

        // Enqueue all work requests to run in parallel
        workManager.enqueue(requests)

        // Return the list of work request IDs
        return requests.map { it.id }
    }

    /**
     * Cancel work by tag.
     *
     * @param context The application context.
     * @param tag The tag of the work to cancel.
     */
    fun cancelWorkByTag(context: Context, tag: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag)
    }

    /**
     * Cancel work by ID (UUID).
     * @param context The application context.
     * @param workId The ID of the work to cancel.
     */
    fun cancelWorkById(context: Context, workId: UUID) {
        WorkManager.getInstance(context).cancelWorkById(workId)
    }

    /**
     * Cancel all work.
     *
     * @param context The application context.
     */
    fun cancelAllWork(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
    }

    /**
     * Observe the status of a work by its ID.
     *
     * @param context The application context.
     * @param workId The ID of the work to observe.
     * @param observer The observer to observe the work status.
     */
    fun observeWorkStatus(context: Context, workId: UUID, observer: (WorkInfo) -> Unit) {
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(workId)
            .observeForever { workInfo ->
                workInfo?.let { observer(it) }
            }
    }

    // Save the work ID to SharedPreferences to use it across entire app
    fun saveWorkIdToSharedPreferences(context: Context, workerIdKey: String, workIdValue: UUID) {
        val sharedPrefUtil = SharedPrefUtil()
        sharedPrefUtil.writeDataStringToSharedPreferences(context, workerIdKey, workIdValue.toString())
    }

    // Retrieve the saved work ID from SharedPreference to use it across entire app
    fun getWorkIdFromSharedPreferences(context: Context, workerIdKey: String): UUID? {
        val sharedPrefUtil = SharedPrefUtil()
        val workIdString = sharedPrefUtil.getDataStringFromSharedPreferences(context, workerIdKey)
        return workIdString?.let { UUID.fromString(it) }
    }
}