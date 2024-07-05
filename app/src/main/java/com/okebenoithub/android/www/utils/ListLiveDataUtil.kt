package com.okebenoithub.android.www.utils
import androidx.lifecycle.MutableLiveData

/**
 * ListLiveDataUtil: Listen to ListLiveData list changes
 */
class ListLiveDataUtil<T> : MutableLiveData<ArrayList<T>>() {
    init {
        value = ArrayList()
    }
    // Add item
    fun add(item: T) {
        val items: ArrayList<T>? = value
        items!!.add(item)
        value = items
    }
    // Add all items from list
    fun addAll(list: List<T>) {
        val items: ArrayList<T>? = value
        items!!.addAll(list)
        value = items
    }
    // Clear all items
    fun clear(notify: Boolean) {
        val items: ArrayList<T>? = value
        items!!.clear()
        if (notify) {
            value = items
        }
    }
    // Remove item
    fun remove(item: T) {
        val items: ArrayList<T>? = value
        items!!.remove(item)
        value = items
    }
    // notify all changes
    fun notifyChange() {
        val items: ArrayList<T>? = value
        value = items
    }
}