package com.example.autoparts_catalog

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (FirebaseApp.getApps(this).isNotEmpty()) {
            Log.d("App", "Firebase initialized")
        } else {
            Log.e("App", "Firebase initialization failed")
        }
    }
}