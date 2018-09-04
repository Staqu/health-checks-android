package com.staqu.healthmonitoring.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {


    companion object {
        private var retrofit: Retrofit? = null
        private var okHttpClient: OkHttpClient? = null

        private const val REQUEST_TIMEOUT: Long = 60L

        fun getClient(context: Context): Retrofit? {

            if (okHttpClient == null)
                initOkHttp(context)

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(Const.BASE_URL)
                        .client(okHttpClient)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }

        private fun initOkHttp(context: Context) {

            val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = (HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(interceptor)

            httpClient.addInterceptor(Interceptor {

                val original: Request = it.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")

                // Adding Authorization token (API Key)
                // Requests will be denied without API key
                //if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
                requestBuilder.addHeader("X-Api-Key", "YOUR_API_KEY")
                //}

                val request: Request = requestBuilder.build()
                it.proceed(request)
            })

            okHttpClient = httpClient.build()
        }


    }

}



