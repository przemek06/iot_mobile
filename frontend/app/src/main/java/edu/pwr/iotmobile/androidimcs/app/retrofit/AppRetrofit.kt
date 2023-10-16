package edu.pwr.iotmobile.androidimcs.app.retrofit

import retrofit2.GsonConverterFactory
import retrofit2.Retrofit

object AppRetrofit {
    private const val BASE_URL = "http://localhost:8080"

    fun create(): Retrofit {
        return Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
}
