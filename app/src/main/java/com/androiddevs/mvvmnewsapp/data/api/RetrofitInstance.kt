package com.androiddevs.mvvmnewsapp.data.api

import com.androiddevs.mvvmnewsapp.application.MyApplication
import com.androiddevs.mvvmnewsapp.utils.Constants.Companion.BASE_URL
import com.tailors.doctoria.utils.InternetConnection
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {
        val cacheSize: Long = (2 * 1024 * 1024).toLong()
        val HEADER_CACHE_CONTROL: String = "Cache-Control"
        val HEADER_PRAGMA: String = "Pragma"
        private fun cache(): Cache {
            return Cache(
                File(MyApplication.myAppContext.cacheDir, "someIdentifier"),
                cacheSize)
        }
        private fun offlineInterceptor(): Interceptor {
            return object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request: Request = chain.request()
                    if (InternetConnection().hasInternetConnection().not()) {
                        val cacheControl: CacheControl = CacheControl.Builder()
                            .build()
                        request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build()
                    }
                    return chain.proceed(request)
                }
            }
        }

        private fun networkInterceptor(): Interceptor {
            return Interceptor { chain: Interceptor.Chain ->
                val response = chain.proceed(chain.request())
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.SECONDS)
                    .build()
                response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
            }
        }

        private val retrofit by lazy {
            val client: OkHttpClient = OkHttpClient().newBuilder()
                .cache(cache())
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor())
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api: NewsApi by lazy {
            retrofit.create(NewsApi::class.java)
        }

    }
}