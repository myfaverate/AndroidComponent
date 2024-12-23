package edu.tyut.componentpluginuse

import android.app.Application
import android.util.Log
import edu.tyut.featurehome.HomeApplication
import edu.tyut.featureuser.UserApplication

private const val TAG: String = "MainApplication"

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate...")
        HomeApplication().onCreate()
        UserApplication().onCreate()
    }
}