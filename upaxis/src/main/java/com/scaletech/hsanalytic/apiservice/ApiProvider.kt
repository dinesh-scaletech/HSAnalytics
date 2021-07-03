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

    private  val BASE_URL:String = "https://dev.trcked.me/"
    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with app api Base url.
     *
     * @return instance of ad retrofit.
     */
    private var retrofit: Retrofit? = null
        private get() = if (field != null) {
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
    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message: String? -> {
        message?.let { Log.e("response", it) }
    } }).setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient: OkHttpClient?
        private get() {
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
        private get() {
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
        private get() = if (retrofitDynamic != null) {
            retrofitDynamic
        } else {
            retrofitDynamic = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientDynamic)
                .build()
            retrofitDynamic
        }

    fun <S> createService(serviceClass: Class<S>?): S? {
        return retrofit?.create(serviceClass)
    }

    fun <S> createServiceString(serviceClass: Class<S>?): S? {
        return retrofitString?.create(serviceClass)
    }

    fun <S> createServiceDynamic(serviceClass: Class<S>?): S? {
        return retrofitDynamicUrl?.create(serviceClass)
    }
}