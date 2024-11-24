package com.okebenoithub.android.www.components

import android.app.Application
import com.kdownloader.KDownloader
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp

// Application class
@HiltAndroidApp
class App : Application() {
    lateinit var kDownloader: KDownloader
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build() // initialize Hawk for object store
        // Initialize KDownloader
        kDownloader = KDownloader.create(applicationContext)
    }
}