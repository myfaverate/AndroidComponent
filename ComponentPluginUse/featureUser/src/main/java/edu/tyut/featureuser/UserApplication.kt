package edu.tyut.featureuser

import android.app.Application
import android.util.Log

private const val TAG: String = "UserApplication"

class UserApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate...")
    }
}