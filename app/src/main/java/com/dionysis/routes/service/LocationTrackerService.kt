package com.dionysis.routes.service

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dionysis.routes.CHANNEL_ID
import com.dionysis.routes.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationTrackerService : Service() {
    @Inject
    lateinit var locationManager: LocationManager
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    enum class Action {
        START, STOP
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            Action.START.name -> start()
            Action.STOP.name -> stop()
        }
        return START_STICKY
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Location Updates")
            .setContentText("Waiting for location...")
            .build()

        // Start the service as a foreground service
        startForeground(1, notification)

        // Launch coroutine to collect location updates
        scope.launch {
            locationManager.trackLocation().collect { location ->
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()

                // Only update notification if the permission is granted (API 33+ check)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(this@LocationTrackerService, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

                    // Update notification with the new location
                    val updatedNotification = NotificationCompat.Builder(this@LocationTrackerService, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Location Updates")
                        .setContentText("Location: ..$latitude / ..$longitude")
                        .build()
                    startForeground(1, updatedNotification)
                }
            }
        }
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        scope.cancel() // Clean up coroutine scope
    }


}