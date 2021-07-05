package com.scaletech.hsanalytic.apiservice

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.QueryMap
public interface ApiInterface {

    @POST("/postback ")
    @JvmSuppressWildcards
    fun  trackRequest(@QueryMap queryParams:Map<String,Any?>):Call<String>
}