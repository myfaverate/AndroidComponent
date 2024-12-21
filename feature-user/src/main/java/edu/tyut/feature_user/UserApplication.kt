package edu.tyut.feature_user

import android.app.Application
import android.util.Log

private const val TAG: String = "UserApplication"

class UserApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate...")
    }
    override fun onTerminate() {
        super.onTerminate()
        Log.i(TAG, "onTerminate -> exit...")
    }
}