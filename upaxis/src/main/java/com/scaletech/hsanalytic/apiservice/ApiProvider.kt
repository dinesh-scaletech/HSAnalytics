package com.scaletech.hsanalytic.apiservice

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

public class ApiProvider {
    private val BASE_URL: String = "https://dev.trcked.me/"
    companion object {
        private val provider = ApiProvider()

        fun createService(): ApiInterface? {
            return provider.retrofit?.create(ApiInterface::class.java)
        }

        fun  createServiceString(): ApiInterface? {
            return provider.retrofitString?.create(ApiInterface::class.java)
        }

        fun  createServiceDynamic(): ApiInterface? {
            return provider.retrofitDynamicUrl?.create(ApiInterface::class.java)
        }

    }


    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with app api Base url.
     *
     * @return instance of ad retrofit.
     */
    private var retrofit: Retrofit? = null
        get() = if (field != null) {
            field
        } else {
            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()
            field = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            field
        }

    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with app api Base url.
     *
     * @return instance of ad retrofit.
     */
    private var retrofitString: Retrofit? = null
        private get() = if (field != null) {
            field
        } else {
            field = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
            field
        }
    private var retrofitDynamic: Retrofit? = null
    private var okHttpClientDynamic: OkHttpClient? = null
    private var okHttpClient: OkHttpClient? = null
    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor { message: String? ->
        run {
            message?.let { Log.e("response", it) }
        }
    }.setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient: OkHttpClient?
        get() {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .build()
            }
            return okHttpClient
        }
    private val httpClientDynamic: OkHttpClient?
        get() {
            if (okHttpClientDynamic == null) {
                okHttpClientDynamic = OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(false)
                    .build()
            }
            return okHttpClientDynamic
        }

    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with app api Base url.
     *
     * @return instance of ad retrofit.
     */
    private val retrofitDynamicUrl: Retrofit?
        get() = if (retrofitDynamic != null) {
            retrofitDynamic
        } else {
            retrofitDynamic = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientDynamic)
                .build()
            retrofitDynamic
        }


}