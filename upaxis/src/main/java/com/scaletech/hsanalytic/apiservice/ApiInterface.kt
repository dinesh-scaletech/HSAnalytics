package com.scaletech.hsanalytic.apiservice

import retrofit2.Call
import retrofit2.http.*
public interface ApiInterface {

    @POST("/postback")
    fun  postEvent(@Body bodyParams:HashMap<String,Any>):Call<Void>
}