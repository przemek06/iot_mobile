package edu.pwr.iotmobile.androidimcs.app.retrofit

import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object AppRetrofit {
    private const val BASE_URL = "http://192.168.66.46:8080/"

    fun create(
        cookiesInterceptor: AddCookiesInterceptor
    ): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(cookiesInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit
            .Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
}

class AddCookiesInterceptor(
    private val userLocalDataSource: UserLocalDataSource
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        runBlocking {
            val activeUserCookie = userLocalDataSource.userSessionCookie.firstOrNull()
            activeUserCookie?.let {
                builder.addHeader("Cookie", it)
            }
        }

        return chain.proceed(builder.build())
    }
}