package com.upaxis.apiservice

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

public interface ApiInterface {

    @POST("/postback")
    fun  postEvent(@Body bodyParams:HashMap<String,Any>):Call<Void>
}