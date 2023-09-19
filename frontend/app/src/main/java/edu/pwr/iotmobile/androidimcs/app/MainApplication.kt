package edu.pwr.iotmobile.androidimcs.app

import android.app.Application
import edu.pwr.iotmobile.androidimcs.app.koin.AppKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppKoin.init(this)
    }
}
