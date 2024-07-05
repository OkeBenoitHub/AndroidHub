package com.okebenoithub.android.www.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore Util
 * This class is used to store and get data from Jetpack DataStore
 */
class DataStorePrefUtil(val context: Context, storeInstance: String) {
    private val Context.dataStore by preferencesDataStore(storeInstance)

    // Function to save a string value
    suspend fun saveString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // Function to get a string value
    fun getString(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }

    // Function to save an integer value
    suspend fun saveInt(key: String, value: Int) {
        val dataStoreKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // Function to get an integer value
    fun getInt(key: String): Flow<Int?> {
        val dataStoreKey = intPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }

    // Function to save a boolean value
    suspend fun saveBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // Function to get a boolean value
    fun getBoolean(key: String): Flow<Boolean?> {
        val dataStoreKey = booleanPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }

    // Function to remove a value
    suspend fun remove(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(dataStoreKey)
        }
    }

    // Function to clear all values
    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}