package com.okebenoithub.android.www.ui.fragments.workmanager

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.WorkInfo
import com.google.firebase.perf.trace
import com.okebenoithub.android.www.components.Event
import com.okebenoithub.android.www.utils.workmanager.WorkManagerUtil
import com.okebenoithub.android.www.utils.workmanager.Worker1
import com.okebenoithub.android.www.utils.workmanager.Worker2
import java.util.UUID

class WorkManagerViewModel : ViewModel() {

    /**
     * Schedule a one-time work request.
     * @param context The application context.
     */
    fun scheduleOneTimeWork(context: Context, workTag: String? = null): UUID {
        val workClass = Worker1::class.java
        // Scheduling a one-time work request
        val inputData = Data.Builder()
            .putString("key_string", "Hello, World!")
            .putInt("key_int", 123)
            .putBoolean("key_boolean", true)
            .build()

        // Define constraints :: this work will only run if the device is connected to the internet and the battery is not low
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        return WorkManagerUtil.scheduleOneTimeWork(context, workClass, inputData, workTag, constraints)
    }

    /**
     * Schedule a periodic work request.
     * @param context The application context.
     */
    fun schedulePeriodicWork(context: Context, workTag: String? = null): UUID {
        val workClass = Worker1::class.java
        // Scheduling a periodic work request
        val repeatInterval = 15L // 15 minutes

        val inputData = Data.Builder()
            .putString("key_string", "Hello, World!")
            .putInt("key_int", 123)
            .putBoolean("key_boolean", true)
            .build()

        return WorkManagerUtil.schedulePeriodicWork(context, workClass, repeatInterval, inputData, workTag)
    }

    /**
     * Schedule multiple works in parallel.
     * @param context The application context.
     */
    fun scheduleMultipleWorks(context: Context, work1Tag: String? = null, work2Tag: String? = null): List<UUID> {

        // set input data for work 1
        val inputData1 = Data.Builder()
            .putString("key_string", "Hello, World!")
            .putInt("key_int", 123)
            .putBoolean("key_boolean", true)
            .build()

        // set input data for each work 2
        val inputData2 = Data.Builder()
            .putString("key_string", "Hello, World!")
            .putInt("key_int", 123)
            .putBoolean("key_boolean", true)
            .build()

        val workRequests = listOf(
            Triple(Worker1::class.java, inputData1, work1Tag),
            Triple(Worker2::class.java, inputData2, work2Tag)
        )
        // Scheduling multiple works
        return WorkManagerUtil.scheduleMultipleWorks(context, workRequests)
    }

    /**
     * Cancel work by tag.
     * @param context The application context.
     * @param workTag The tag of the work to cancel.
     */
    fun cancelWorkByTag(context: Context, workTag: String) {
        WorkManagerUtil.cancelWorkByTag(context, workTag)
    }

    /**
     * Cancel work by ID.
     * @param context The application context.
     * @param workId The ID of the work to cancel.
     */
    fun cancelWorkById(context: Context, workId: UUID) {
        WorkManagerUtil.cancelWorkById(context, workId)
    }

    /**
     * Cancel all work.
     * @param context The application context.
     */
    fun cancelAllWork(context: Context) {
        WorkManagerUtil.cancelAllWork(context)
    }

    // LiveData to observe work status
    private val _workStatusObserver = MutableLiveData<Event<WorkInfo>>()
    val workStatusObserver: MutableLiveData<Event<WorkInfo>>
        get() = _workStatusObserver

    /**
     * Observe the status of a work by its ID.
     * @param context The application context.
     * @param workId The ID of the work to observe.
     */
    fun observeWorkStatus(context: Context, workId: UUID) {
        WorkManagerUtil.observeWorkStatus(context, workId) { workInfo ->
            _workStatusObserver.value = Event(workInfo)
        }
    }
}