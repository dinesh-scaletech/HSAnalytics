package com.scaletech.hsanalytic.apiservice

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

public class ApiProvider {
    companion object {
        private val provider = ApiProvider()
        fun  createServiceString(baseUrl:String): ApiInterface? {
            return provider.retrofitString(baseUrl)?.create(ApiInterface::class.java)
        }
    }

    private fun retrofitString(baseUrl: String): Retrofit? {
       return  Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    }

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

}