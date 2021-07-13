package com.scaletech.hsanalytic.apiservice

import android.util.Log
import com.scaletech.hsanalytic.UpAxisConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

public class ApiProvider {
    private var okHttpClient: OkHttpClient? = null

    companion object {
        private val provider = ApiProvider()
        fun createServiceString(): ApiInterface? {
            return provider.retrofitString()?.create(ApiInterface::class.java)
        }
    }

    private fun retrofitString(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(UpAxisConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor { message: String? ->
        run {
            message?.let {
                Log.e("response", it)
            }
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