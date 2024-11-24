package com.okebenoithub.android.www.ui.fragments.workmanager

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.WorkInfo
import com.okebenoithub.android.www.databinding.FragmentWorkManagerBinding
import com.okebenoithub.android.www.utils.MainUtil
import com.okebenoithub.android.www.utils.PermissionsUtil
import com.okebenoithub.android.www.utils.alarm.AlarmManagerUtil
import com.okebenoithub.android.www.utils.workmanager.WorkManagerUtil
import java.util.UUID

class WorkManagerFragment : Fragment() {

    private val viewModel: WorkManagerViewModel by viewModels()
    private lateinit var binding: FragmentWorkManagerBinding

    // Run time permissions
    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1001
        val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.WAKE_LOCK,
            android.Manifest.permission.REBOOT
        )
    }

    // Activity Result Launcher for requesting permissions
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var isRequiredPermissionsGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkManagerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // start one time work
        binding.oneTimeWork.setOnClickListener {
            val workId = viewModel.scheduleOneTimeWork(requireContext())
            startAndObserveWork(workId, "work_key1")
        }

        // start periodic work
        binding.periodicWork.setOnClickListener {
            val workId = viewModel.schedulePeriodicWork(requireContext())
            startAndObserveWork(workId, "work_key2")
        }

        // start multiple works
        binding.multipleWork.setOnClickListener {
            val workIds = viewModel.scheduleMultipleWorks(requireContext())
            val workIdsPrefKeys = listOf("work_key3", "work_key4", "work_key5")
            workIds.forEach { workId ->
                startAndObserveWork(workId, workIdsPrefKeys[workIds.indexOf(workId)])
            }
        }

        // observe work status
        viewModel.workStatusObserver.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { workInfo -> observeWorkStatus(workInfo) }
        }

        return binding.root
    }

    /**
     * Start and observe work.
     */
    private fun startAndObserveWork(workIdValue: UUID, workIdPrefKey: String) {
        // save workId to shared preferences
        WorkManagerUtil.saveWorkIdToSharedPreferences(requireContext(), workIdPrefKey, workIdValue)
        // observe work status
        viewModel.observeWorkStatus(requireContext(), workIdValue)
    }

    private fun observeWorkStatus(workInfo: WorkInfo) {
        val workId = workInfo.id
        // do something with workInfo
        when(workInfo.state) {
            WorkInfo.State.ENQUEUED -> {
                // work has been enqueued
            }
            WorkInfo.State.RUNNING -> {
                // work is running
            }
            WorkInfo.State.SUCCEEDED -> {
                // work has succeeded :: get work output data
                val outputData = workInfo.outputData
                if (outputData.keyValueMap.isNotEmpty()) {
                    // do something with output data
                    val outputDataString = outputData.getString("output_key")
                    val outputDataInt = outputData.getInt("output_key", -1)
                }

            }
            WorkInfo.State.FAILED -> {
                // work has failed
            }
            WorkInfo.State.BLOCKED -> {
                // work has been blocked
            }
            WorkInfo.State.CANCELLED -> {
                // work has been cancelled
            }
            else -> {
                // work has other state
            }
        }
    }

    private fun setAlarmManager(context: Context) {
        // Setting a one-time alarm to trigger at a specific time
        AlarmManagerUtil.setOneTimeAlarm(context, System.currentTimeMillis() + 60 * 1000)

        // Setting a repeating alarm to trigger every 15 minutes
        AlarmManagerUtil.setRepeatingAlarm(context, 15 * 60 * 1000)

        // Canceling the alarm
        AlarmManagerUtil.cancelAlarm(context)
    }

    private fun checkForRequiredPermissions() {
        // Check if all required permissions are granted
        // Initialize the permission launcher
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                // Permissions were granted, proceed with your task
                isRequiredPermissionsGranted = true
            } else {
                // Request permissions
                PermissionsUtil.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }
    }
}