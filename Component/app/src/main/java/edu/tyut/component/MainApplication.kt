package edu.tyut.component

import android.app.Application
import android.util.Log
import edu.tyut.feature_home.HomeApplication
import edu.tyut.feature_user.UserActivity
import edu.tyut.feature_user.UserApplication

private const val TAG: String = "MainApplication"

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HomeApplication().onCreate()
        UserApplication().onCreate()
        Log.i(TAG, "onCreate...")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i(TAG, "onTerminate -> exit...")
    }
}