package com.okebenoithub.android.www.ui.fragments.kotlinFlows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okebenoithub.android.www.components.Event
import com.okebenoithub.android.www.utils.FlowUtil
import com.okebenoithub.android.www.utils.FlowUtil.asFlow
import com.okebenoithub.android.www.utils.FlowUtil.asLiveData
import com.okebenoithub.android.www.utils.FlowUtil.collectFlow
import com.okebenoithub.android.www.utils.FlowUtil.combineFlows
import com.okebenoithub.android.www.utils.FlowUtil.createFlow
import com.okebenoithub.android.www.utils.FlowUtil.debounceFlow
import com.okebenoithub.android.www.utils.FlowUtil.handleErrorsAndCompletion
import com.okebenoithub.android.www.utils.FlowUtil.logEmissions
import com.okebenoithub.android.www.utils.FlowUtil.retryOnError
import com.okebenoithub.android.www.utils.FlowUtil.throttleFirst
import com.okebenoithub.android.www.utils.FlowUtil.transformFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class KotlinFlowsViewModel : ViewModel() {
    // create a list of strings
    private val data = listOf("Item 1", "Item 2", "Item 3", "Item 4")

    // create list of 50 random ints
    private val data2 = List(50) { (0..100).random() }

    private val _uiState: MutableStateFlow<String> = MutableStateFlow("")
    val uiState: StateFlow<String> get() = _uiState
    private var collectJob: Job? = null

    // LiveData to Flow example
    val liveData = MutableLiveData("Initial LiveData")
    val flowData: Flow<String> = liveData.asFlow()
    val transformedLiveData: LiveData<String> =
        flowData.transformFlow { value -> "Transformed $value" }.asLiveData(viewModelScope)

    // collect flow
    fun collectFlow(throttle: Long = 0L, debounce: Long = 0L, takeEmissions: Int = 0) {
        // Using FlowUtil to create, transform, and collect flow
        collectJob = viewModelScope.collectFlow(
            flow = createFlow(data)
                .transformFlow { item -> "Transformed $item" }
                .logEmissions("FlowLog")
                .handleErrorsAndCompletion(
                    onError = { e -> _uiState.value = "Error: ${e.message}" },
                    onCompletion = { _uiState.value = "Completed" }
                )
                .retryOnError(retries = 3, delayMillis = 1000),
            onEach = { value ->
                // on each emission, update the UI state
                _uiState.value = value
            }
        )
    }

    // stop collecting flow
    fun stopCollecting() {
        collectJob?.cancel()
    }

    // Example of using debounce and throttle
    fun collectDebouncedAndThrottledFlow() {
        viewModelScope.collectFlow(
            flow = createFlow(data)
                .debounceFlow(1000)
                .throttleFirst(1000),
            onEach = { value ->
                _uiState.value = "Debounced and Throttled: $value"
            }
        )
    }

    // Example of combining two flows
    private val combinedFlow =
        combineFlows(flow1 = createFlow(data), flow2 = createFlow(data2)) { item1, item2 ->
            "$item1 - $item2"
        }

    // collect two combined flows
    fun collectCombinedFlows() {
        // Collecting the combined flow
        viewModelScope.collectFlow(
            flow = combinedFlow.logEmissions("CombinedFlow"),
            onEach = { value ->
                _uiState.value = "Combined Flow Emission: $value"
            }
        )
    }

    // create a flow of integers
    private val intFlow: Flow<Int> = flow {
        for (i in 1..100) {
            emit(i)
            delay(1000L)
        }
    }

    // create a flow of strings
    private val stringFlow: Flow<String> = flow {
        for (i in 1..100) {
            emit("Hello $i")
            delay(1000L)
        }
    }
}