package com.okebenoithub.android.www.utils

import com.orhanobut.hawk.Hawk

/**
 * Object Store Util
 * This class is used to store and get data from Hawk
 * You can use this class to store and retrieve complex data structures
 * Before using this class, you need to initialize it in your application class
 * Hawk.init(this).build()
 */
class ObjectStorePrefUtil {

    // Save a single object
    fun <T> saveObject(key: String, obj: T) {
        Hawk.put(key, obj)
    }

    // Retrieve a single object
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        return Hawk.get(key, null)
    }

    // Save a list of objects
    fun <T> saveObjectList(key: String, objList: List<T>) {
        Hawk.put(key, objList)
    }

    // Retrieve a list of objects
    fun <T> getObjectList(key: String, clazz: Class<T>): List<T>? {
        return Hawk.get(key, null)
    }

    // Check if a key exists
    fun exists(key: String): Boolean {
        return Hawk.contains(key)
    }

    // Remove a specific key
    fun delete(key: String) {
        Hawk.delete(key)
    }

    // Clear all preferences
    fun clearAll() {
        Hawk.deleteAll()
    }

    private fun HowToUse() {
        // Example usage
        // val objectStorePrefUtil = ObjectStorePrefUtil()
        // val myObject = MyObject("Example", 123)
        // objectStorePrefUtil.saveObject("myObjectKey", myObject)

        // val retrievedObject: MyObject? = objectStorePrefUtil.getObject("myObjectKey", MyObject::class.java)
        /*
        retrievedObject?.let {
            // Do something with the retrieved object
        }
        */

        // Check existence
        // val exists = objectStorePrefUtil.exists("myObjectKey")

        // Delete a specific object
        // objectStorePrefUtil.delete("myObjectKey")

        // Clear all preferences
        // objectStorePrefUtil.clearAll()
    }
}