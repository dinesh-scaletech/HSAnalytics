package com.scaletech.hsanalytic

import android.util.Log
import com.scaletech.hsanalytic.apiservice.ApiInterface
import com.scaletech.hsanalytic.apiservice.ApiProvider
import com.scaletech.hsanalytic.utils.PARAM_CLICK_ID
import com.scaletech.hsanalytic.utils.PARAM_DUPLICATE
import com.scaletech.hsanalytic.utils.PARAM_K
import retrofit2.Call
import retrofit2.Response

public class UpAxis {
    /**
     * Method to track user action and provide info based on user information.
     */
    @JvmSuppressWildcards
    fun trackUser(k: String, clickId: String, allowDuplicate: Boolean = false) {
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any?>()
        queryParams[PARAM_K] = k
        queryParams[PARAM_CLICK_ID] = clickId
        queryParams[PARAM_DUPLICATE] = if (allowDuplicate) 1 else 0
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
