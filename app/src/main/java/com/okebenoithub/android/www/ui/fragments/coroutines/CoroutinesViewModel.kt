package com.okebenoithub.android.www.ui.fragments.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okebenoithub.android.www.utils.CoroutineUtil
import com.okebenoithub.android.www.utils.CoroutineUtil.withTimeoutOrDefault
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class CoroutinesViewModel : ViewModel() {

    private val _uiState = MutableLiveData<String>()
    val uiState: LiveData<String> get() = _uiState

    // coroutine jobs
    private var launchOnMainJob: Job? = null
    private var launchOnIOJob: Job? = null
    private var launchOnDefaultJob: Job? = null
    private var launchWithExceptionHandlerJob: Job? = null
    private var launchWithRetryJob: Job? = null
    private var launchWithFlowCollectorJob: Job? = null
    var launchConcurrentlyJob: Job? = null
    private var launchWithTimeoutJob: Job? = null

    fun launchOnMainThread() {
        launchOnMainJob = CoroutineUtil.launchOnMain {
            // Code that runs on the main thread
            _uiState.value = "Main Thread"
        }
    }

    fun launchOnIOThread() {
        launchOnIOJob = CoroutineUtil.launchOnIO {
            // Code that runs on the IO thread
            _uiState.value = "IO Thread"
        }
    }

    fun launchOnDefaultThread() {
        launchOnDefaultJob = CoroutineUtil.launchOnDefault {
            // Code that runs on the Default thread
            _uiState.value = "Default Thread"
        }
    }

    // Example: Launch with exception handler
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: $throwable")
    }

    fun launchWithExceptionHandler() {
        launchWithExceptionHandlerJob = CoroutineUtil.launchWithExceptionHandler(exceptionHandler = exceptionHandler) {
            // Code that might throw an exception
        }
    }

    // Example: Retry a request
    fun launchWithRetryRequest(timesRetry: Int) {
        launchWithRetryJob = CoroutineUtil.launchOnIO {
            val result = CoroutineUtil.retry(times = timesRetry) {
                // Network request or other suspend function
            }
        }
    }

    // Example: Launch flow collector
    // Example: Launch flow collector
    private val flow = flowOf("Item 1", "Item 2", "Item 3")
    fun launchWithFlowCollector() {
        launchWithFlowCollectorJob = CoroutineUtil.launchFlowCollector(
            flow = flow,
            scope = viewModelScope,
            onEach = { item ->
                // Code that runs on the main thread
            },
            onError = { error ->
                // Code that runs on the main thread
            },
            onComplete = {
                // Code that runs on the main thread
            }
        )
    }

    // Example: Debounce a function
    fun launchWithDebounce(waitedMs: Long) {
        // Example: Debounce a function
        val debouncedFunction = CoroutineUtil.debounce(waitedMs, viewModelScope) { value: String ->
            println("Debounced value: $value")
        }
        debouncedFunction("Value 1")
        debouncedFunction("Value 2")
    }

    // Example: Throttle a function
    fun launchWithThrottle(waitedMs: Long) {
        val throttledFunction =
            CoroutineUtil.throttleFirst(waitedMs, viewModelScope) { value: String ->
                println("Throttled value: $value")
            }
        throttledFunction("Value 1")
        throttledFunction("Value 2")
    }

    // Example: Launch with timeout
    fun launchWithTimeout(timeMillis: Long) {
        launchWithTimeoutJob = viewModelScope.launch {
            // Example: Launch with timeout
            val deferred = viewModelScope.withTimeoutOrDefault(timeMillis, "Default") {
                // Suspend function that might take too long
                delay(3000)
                "Result"
            }
            viewModelScope.launch {
                val result = deferred.await()
                println("Result with timeout: $result")
            }
        }
    }

    /**
     * Example: Run a list of suspend functions concurrently
     */
    private suspend fun work1() { // work 1 in the background
        delay(1000)
        for (i in 1..1000) {
            delay(1000)
            println("Work 1: $i")
            _uiState.value += "Work 1: $i"
        }
    }

    private suspend fun work2() { // work 2 in the background
        delay(2000)
        for (i in 1..2000) {
            delay(2000)
            println("Work 2: $i")
            _uiState.value += "Work 2: $i"
        }
    }

    private suspend fun work3() { // work 3 in the background
        delay(3000)
        for (i in 1..3000) {
            delay(3000)
            println("Work 3: $i")
            _uiState.value += "Work 3: $i"
        }
    }


    // Example: Run a list of suspend functions concurrently
    fun launchWithConcurrently() {
        launchConcurrentlyJob = viewModelScope.launch {
            val result = CoroutineUtil.runConcurrently(
                { work1() },
                { work2() },
                { work3() }
            )
        }
    }

    // Example: Cancel a coroutine job
    fun cancelJob(coroutineJob: Job?) {
        CoroutineUtil.cancelJob(coroutineJob)
    }
}