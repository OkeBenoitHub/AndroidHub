package com.okebenoithub.android.www.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * A utility class for various flow-related operations.
 */
object FlowUtil {

    // Example function to create a simple flow
    fun <T> createFlow(data: List<T>): Flow<T> = flow {
        for (item in data) {
            emit(item)
            delay(500) // Simulating delay
        }
    }

    // Function to collect a flow with a given CoroutineScope and provide a way to stop collecting
    fun <T> CoroutineScope.collectFlow(
        flow: Flow<T>,
        onEach: (T) -> Unit,
        onCompletion: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Job {
        return this.launch {
            flow
                .catch { e -> onError(e) }
                .onCompletion { onCompletion() }
                .collect { value ->
                    withContext(Dispatchers.Main) {
                        onEach(value)
                    }
                }
        }
    }

    // Function to handle flow errors and completion
    fun <T> Flow<T>.handleErrorsAndCompletion(
        onError: (Throwable) -> Unit,
        onCompletion: () -> Unit
    ): Flow<T> = this
        .catch { e -> onError(e) }
        .onCompletion { onCompletion() }

    // Function to take only a certain number of emissions
    fun <T> Flow<T>.takeEmissions(count: Int): Flow<T> = this.take(count)

    // Function to apply a transformation to the flow
    fun <T, R> Flow<T>.transformFlow(transform: suspend (T) -> R): Flow<R> = this.map { value ->
        transform(value)
    }

    // Function to log flow emissions for debugging
    fun <T> Flow<T>.logEmissions(tag: String): Flow<T> = this.onEach { value ->
        println("$tag: $value")
    }

    // Function to convert a Flow to LiveData
    fun <T> Flow<T>.asLiveData(scope: CoroutineScope): LiveData<T> {
        val liveData = MutableLiveData<T>()
        scope.launch {
            this@asLiveData.collect {
                liveData.postValue(it)
            }
        }
        return liveData
    }

    // Function to convert LiveData to Flow
    fun <T> LiveData<T>.asFlow(): Flow<T> = flow {
        emitSource(this@asFlow)
    }

    private suspend fun <T> FlowCollector<T>.emitSource(liveData: LiveData<T>) {
        val observer = Observer<T> {
            // Emit the value to the flow in coroutine context
            flow<T> { this@emitSource.emit(it) }
        }

        liveData.observeForever(observer)
        try {
            kotlinx.coroutines.awaitCancellation()
        } finally {
            liveData.removeObserver(observer)
        }
    }

    // Function to debounce a flow
    // DebounceFlow applies a debounce mechanism to the flow to emit items
    // only if a specified timeout has passed between emissions.
    @OptIn(FlowPreview::class)
    fun <T> Flow<T>.debounceFlow(timeout: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Flow<T> {
        return this.debounce(timeUnit.toMillis(timeout))
    }

    // Function to throttle a flow
    // throttleFirst throttles the flow to emit only the first item within the specified timeout interval.
    fun <T> Flow<T>.throttleFirst(timeout: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Flow<T> {
        var lastEmissionTime = 0L
        return this.transform { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime > timeUnit.toMillis(timeout)) {
                emit(value)
                lastEmissionTime = currentTime
            }
        }
    }

    // Function to retry a flow on error
    fun <T> Flow<T>.retryOnError(
        retries: Int = 3,
        delayMillis: Long = 1000,
        shouldRetry: (Throwable) -> Boolean = { true }
    ): Flow<T> {
        return this.retryWhen { cause, attempt ->
            if (attempt < retries && shouldRetry(cause)) {
                delay(delayMillis)
                true
            } else {
                false
            }
        }
    }

    // Function to combine two flows
    fun <T1, T2, R> combineFlows(
        flow1: Flow<T1>,
        flow2: Flow<T2>,
        transform: suspend (T1, T2) -> R
    ): Flow<R> {
        return flow1.combine(flow2, transform)
    }
}