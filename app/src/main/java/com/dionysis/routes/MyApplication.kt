package com.dionysis.routes

import android.app.Application
import android.os.Build
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

const val CHANNEL_ID = "channel_id"
const val CHANNEL_NAME = "channel_name"
@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = android.app.NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_MIN
            )
            val notificationManager = getSystemService(android.app.NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }
}