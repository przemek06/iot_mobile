package edu.pwr.iotmobile.androidimcs.app.retrofit

import edu.pwr.iotmobile.androidimcs.BuildConfig
import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserSessionLocalDataSource
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
import java.util.concurrent.TimeUnit

object AppRetrofit {
    private val BASE_URL = "http://${BuildConfig.APP_NETWORK}/"

    fun create(
        cookiesInterceptor: AddCookiesInterceptor
    ): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
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
    private val userSessionLocalDataSource: UserSessionLocalDataSource
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        runBlocking {
            val activeUserCookie = userSessionLocalDataSource.userSessionCookie.firstOrNull()
            activeUserCookie?.let {
                builder.addHeader("Cookie", it)
            }
        }

        return chain.proceed(builder.build())
    }
}