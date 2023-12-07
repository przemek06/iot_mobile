package edu.pwr.iotmobile.androidimcs.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.dto.NotificationDto
import edu.pwr.iotmobile.androidimcs.model.listener.NotificationWebSocketListener
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import kotlin.random.Random

class NotificationService : Service() {

    private var context: Context? = null
    private val client: OkHttpClient by inject()
    private var notificationListener: NotificationWebSocketListener? = null
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("NotifService", "NotificationService onCreate called.")
        this.context = applicationContext
        this.notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
        notificationListener = NotificationWebSocketListener(
            client = client,
            onNotificationReceived = { data -> showBasicNotification(data) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("NotifService", "NotificationService onDestroy called.")
        notificationListener?.closeWebSocket()
    }

    private fun showBasicNotification(data: NotificationDto) {
        val notification = context?.let {
            NotificationCompat.Builder(it, "imcs_notification")
                .setContentTitle(data.title)
                .setContentText(data.description)
                .setSmallIcon(R.drawable.ic_dog_cosmos)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true)
                .build()
        }

        notificationManager?.notify(
            Random.nextInt(),
            notification
        )
    }

    private fun showExpandableNotification(data: NotificationDto){
        val notification = context?.let {
            NotificationCompat.Builder(it, "imcs_notification")
                .setContentTitle(data.title)
                .setContentText(data.description)
                .setSmallIcon(R.drawable.ic_dog_cosmos)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat
                        .BigPictureStyle()
                        .bigPicture(
                            context?.bitmapFromResource(
                                R.drawable.ic_dog_cosmos
                            )
                        )
                )
                .build()
        }
        notificationManager?.notify(Random.nextInt(),notification)
    }

    private fun Context.bitmapFromResource(
        @DrawableRes resId:Int
    )= BitmapFactory.decodeResource(
        resources,
        resId
    )

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}