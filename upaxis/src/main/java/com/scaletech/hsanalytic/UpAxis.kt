package com.scaletech.hsanalytic

import android.content.Context
import android.util.Log
import com.scaletech.hsanalytic.apiservice.ApiInterface
import com.scaletech.hsanalytic.apiservice.ApiProvider
import com.scaletech.hsanalytic.utils.PARAM_CLICK_ID
import com.scaletech.hsanalytic.utils.PARAM_DUPLICATE
import com.scaletech.hsanalytic.utils.PARAM_K
import retrofit2.Call
import retrofit2.Response

public class UpAxis {
    fun trackUser() {
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any?>()
        queryParams[PARAM_K] = "xPGSZ2mMVFsHGQSpg8yk"
        queryParams[PARAM_CLICK_ID] = "17a2ea730f042d5d9aa5bf9b8"
        queryParams[PARAM_DUPLICATE] = 1
        val call = apiInterface?.trackRequest(queryParams)
        call?.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == 200) {
                    response.body()?.let { Log.e("Response", it) }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.message?.let { Log.e("onFailure", it) }
            }

        })

    }


}
