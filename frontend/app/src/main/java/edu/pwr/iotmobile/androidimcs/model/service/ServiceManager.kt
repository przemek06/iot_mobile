package edu.pwr.iotmobile.androidimcs.model.service

import android.Manifest
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object ServiceManager {
    fun serviceStart(context: Context) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notificationChannel= NotificationChannel(
                "imcs_notification",
                "IMCS",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            val serviceIntent = Intent(context, NotificationService::class.java)
            context.startService(serviceIntent)
        }
    }

    fun serviceStop(context: Context) {
        val serviceIntent = Intent(context, NotificationService::class.java)
        context.stopService(serviceIntent)
    }

    fun isServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val services = manager.getRunningServices(Integer.MAX_VALUE)

        for (service in services) {
            if (NotificationService::class.java.name == service.service.className) {
                return true
            }
        }

        return false
    }
}
