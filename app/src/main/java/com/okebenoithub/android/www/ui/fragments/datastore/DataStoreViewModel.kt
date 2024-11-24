package com.okebenoithub.android.www.ui.fragments.datastore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.okebenoithub.android.www.components.Event
import com.okebenoithub.android.www.utils.DataStorePrefUtil
import com.okebenoithub.android.www.utils.MainUtil
import kotlinx.coroutines.launch

class DataStoreViewModel(val app: Application) : AndroidViewModel(app) {
    // TODO: Implement the ViewModel
    val userId = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val userAge = MutableLiveData<String>()
    private val userGender = MutableLiveData<String>()
    val dataStoreTv = MutableLiveData<String>()

    private val _uiState = MutableLiveData<Event<String>>()
    val uiState: LiveData<Event<String>> get() = _uiState

    // TODO: Initialize DataStore
    private val dataStorePrefUtil = DataStorePrefUtil(app.applicationContext, "DataStore1")

    init {
        userId.value = ""
        userName.value = ""
        userEmail.value = ""
        userAge.value = ""
        userGender.value = ""
        dataStoreTv.value = ""
    }

    fun onGenderSelected(gender: Char) {
        when (gender) {
            'M' -> userGender.value = "Male"
            'F' -> userGender.value = "Female"
        }
    }

    fun saveData() {
        // check for user id input
        val userIdValue = userId.value.toString()
        if (userIdValue.isEmpty() || userIdValue.toInt() < 0) {
            _uiState.value = Event("Please enter a valid user id")
            return
        }
        // check for username
        val userNameValue = userName.value.toString()
        if (userNameValue.isEmpty() || !MainUtil().isValidName(userNameValue)) {
            _uiState.value = Event("Please enter a username")
            return
        }

        // check for email
        val userEmailValue = userEmail.value.toString()
        if (userEmailValue.isEmpty() || !MainUtil().isEmailValid(userEmailValue)) {
            _uiState.value = Event("Please enter a valid email")
            return
        }

        // check for age
        val userAgeValue = userAge.value.toString()
        if (userAgeValue.isEmpty() || userAgeValue.toInt() < 0) {
            _uiState.value = Event("Please enter a valid age")
            return
        }

        // check for gender
        val userGenderValue = userGender.value.toString()
        if (userGenderValue.isEmpty()) {
            _uiState.value = Event("Please select a gender")
            return
        }

        // Save data to DataStore
        viewModelScope.launch {
            dataStorePrefUtil.saveString("user_id", userIdValue)
            dataStorePrefUtil.saveString("user_name", userNameValue)
            dataStorePrefUtil.saveString("user_email", userEmailValue)
            dataStorePrefUtil.saveString("user_age", userAgeValue)
            dataStorePrefUtil.saveString("user_gender", userGenderValue)
            dataStoreTv.value = "Data saved successfully"
        }
    }

    fun getData() {
        // TODO: Get data from DataStore
        // get data from DataStore
        viewModelScope.launch {
            dataStorePrefUtil.getString("user_id").collect { value ->
                _uiState.value = Event(value.toString())
            }
            dataStorePrefUtil.getString("user_name").collect { value ->
                _uiState.value = Event(value.toString())
            }
            dataStorePrefUtil.getString("user_email").collect { value ->
                _uiState.value = Event(value.toString())
            }
            dataStorePrefUtil.getString("user_age").collect { value ->
                _uiState.value = Event(value.toString())
            }
            dataStorePrefUtil.getString("user_gender").collect { value ->
                _uiState.value = Event(value.toString())
            }
        }
    }
}

/**
 * Create DataStoreViewModel factory
 */
class DataStoreViewModelFactory(private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DataStoreViewModel(
            app
        ) as T
    }
}