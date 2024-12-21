package edu.tyut.feature_home

import android.app.Application
import android.util.Log

private const val TAG: String = "HomeApplication"

class HomeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate...")
    }
    override fun onTerminate() {
        super.onTerminate()
        Log.i(TAG, "onTerminate -> exit...")
    }
}