package com.okebenoithub.android.www.utils

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext

object CoroutineUtil {

    // Launch a coroutine in the Main scope
    fun launchOnMain(
        context: CoroutineContext = Dispatchers.Main,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = CoroutineScope(context).launch(start = start) { block() }

    // Launch a coroutine in the IO scope
    fun launchOnIO(
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = CoroutineScope(context).launch(start = start) { block() }

    // Launch a coroutine in the Default scope
    fun launchOnDefault(
        context: CoroutineContext = Dispatchers.Default,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = CoroutineScope(context).launch(start = start) { block() }

    // Launch a coroutine with exception handling
    fun launchWithExceptionHandler(
        context: CoroutineContext = Dispatchers.Main,
        exceptionHandler: CoroutineExceptionHandler,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = CoroutineScope(context + exceptionHandler).launch(start = start) { block() }

    // Retry a suspending function with delay
    suspend fun <T> retry(
        times: Int = 3,
        initialDelay: Long = 1000,
        maxDelay: Long = 10000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }

    // Launch a flow collector with error handling
    fun <T> launchFlowCollector(
        flow: Flow<T>,
        scope: CoroutineScope,
        onEach: (T) -> Unit,
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {}
    ): Job = scope.launch {
        flow
            .catch { e -> onError(e) }
            .onCompletion { onComplete() }
            .collect { value -> onEach(value) }
    }

    // Function to debounce a suspending function
    fun <T> debounce(
        waitMs: Long,
        scope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = scope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }

    // Function to throttle a suspending function
    fun <T> throttleFirst(
        waitMs: Long,
        scope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var lastExecutionTime = 0L
        return { param: T ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastExecutionTime >= waitMs) {
                lastExecutionTime = currentTime
                scope.launch {
                    destinationFunction(param)
                }
            }
        }
    }

    // Function to launch a coroutine with a timeout
    fun <T> CoroutineScope.withTimeoutOrDefault(
        timeMillis: Long,
        defaultValue: T,
        block: suspend CoroutineScope.() -> T
    ): Deferred<T> = async {
        try {
            withTimeout(timeMillis) {
                block()
            }
        } catch (e: TimeoutCancellationException) {
            defaultValue
        }
    }

    // Function to run a list of suspend functions concurrently
    suspend fun <T> runConcurrently(vararg blocks: suspend () -> T): List<T> = coroutineScope {
        blocks.map { async { it() } }.awaitAll()
    }

    // Function to cancel a coroutine job
    fun cancelJob(job: Job?) {
        job?.cancel()
    }
}