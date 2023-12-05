package edu.pwr.iotmobile.androidimcs.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

fun serviceStarter(context: Context) {
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