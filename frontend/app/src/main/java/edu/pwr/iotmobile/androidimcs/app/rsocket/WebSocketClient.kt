package edu.pwr.iotmobile.androidimcs.app.rsocket

import edu.pwr.iotmobile.androidimcs.app.retrofit.AddCookiesInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object WebSocketClient {

    fun create(
        cookiesInterceptor: AddCookiesInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(cookiesInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .pingInterval(15, TimeUnit.SECONDS) // Configure ping intervals as needed
            .build()
    }
}
