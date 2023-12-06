package edu.pwr.iotmobile.androidimcs.service

import android.app.ActivityManager
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.dto.NotificationDto
import edu.pwr.iotmobile.androidimcs.model.listener.NotificationWebSocketListener
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import kotlin.random.Random

class NotificationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var context: Context? = null
    private val client: OkHttpClient by inject()
    private val notificationListener: NotificationWebSocketListener = NotificationWebSocketListener(
        client = client,
        onNotificationReceived = {  data -> showBasicNotification(data) }
    )
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        this.context = applicationContext
        this.notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
    }

    fun showBasicNotification(data: NotificationDto) {
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

private fun isServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}