package com.scaletech.hsanalytic

import android.os.Build
import android.util.Log
import com.scaletech.hsanalytic.apiservice.ApiInterface
import com.scaletech.hsanalytic.apiservice.ApiProvider
import com.scaletech.hsanalytic.apiservice.UpAxisCallBack
import com.scaletech.hsanalytic.utils.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

public class UpAxis :UpAxisCallBack<String> {

    /**
     * Method to track user action and provide info based on user information.
     */
    @JvmSuppressWildcards
    public fun trackUser(clickId: String, allowDuplicate: Boolean = false) {
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any?>()
        queryParams[PARAM_K] = UpAxisConfig.AUTH_ID
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


    /**
     * Method to track user action and provide info based on user information.
     */
    @JvmSuppressWildcards
    public fun eventCaptured(clickId: String, eventId: String) {
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any?>()
        queryParams[PARAM_K] = UpAxisConfig.AUTH_ID
        queryParams[PARAM_EVENT_ID] = eventId
        queryParams[PARAM_CLICK_ID] = clickId
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


    /**
     * Method to track user action and provide info based on user information.
     */
    @JvmSuppressWildcards
    public fun sendExtraData(eventId: String,clickId: String, appType: String?) {
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any?>()
        queryParams[PARAM_K] = UpAxisConfig.AUTH_ID
        queryParams[PARAM_EVENT_ID] = eventId
        queryParams[PARAM_CLICK_ID] = clickId
        queryParams[PARAM_DUPLICATE] = 1
        queryParams[PARAM_DETAILS] = getDeviceData(appType)
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

    private fun getDeviceData(appType: String?): JSONObject {
        val deviceData = JSONObject()
        deviceData.put("manufacturer", Build.MANUFACTURER)
        deviceData.put("model", Build.MODEL)
        deviceData.put("platform", "Android")
        deviceData.put("appType", if (appType.isNullOrEmpty()) "Game" else "App")
        return deviceData
    }

    fun upAxisCallBack(callBack:UpAxisCallBack<String>) {}
    override fun onResponse(call: Call<String>?, response: Response<String>?) {

    }

    override fun onFailure(call: Call<String>?, t: Throwable?) {

    }
}
