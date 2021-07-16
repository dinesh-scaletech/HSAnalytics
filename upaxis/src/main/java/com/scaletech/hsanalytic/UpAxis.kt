package com.scaletech.hsanalytic

import android.content.Context
import android.os.Build
import com.scaletech.hsanalytic.apiservice.ApiInterface
import com.scaletech.hsanalytic.apiservice.ApiProvider
import com.scaletech.hsanalytic.apiservice.UpAxisCallBack
import com.scaletech.hsanalytic.model.UpAxisResponse
import com.scaletech.hsanalytic.utils.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

public class UpAxis(private val context: Context) {

    /**
     * Method to track user action and provide info based on user information.
     */
    @JvmSuppressWildcards
    public fun trackUser(clickId: String, allowDuplicate: Boolean = false, upAxisCallBack: UpAxisCallBack<Void>) {

        if (!validateUserData()) {
            upAxisCallBack.validationError("Validation Error")
            return
        }

        if (!context.isNetworkAvailable()) {
            upAxisCallBack.noNetworkAvailable()
            return
        }

        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any>()
        queryParams[PARAM_K] = UpAxisConfig.AUTH_ID!!
        queryParams[PARAM_CLICK_ID] = clickId
        queryParams[PARAM_DUPLICATE] = if (allowDuplicate) 1 else 0
        val call = apiInterface?.postEvent(queryParams)
        call?.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                upAxisCallBack.onResponse(setUpResponseData(response))
            }

            override fun onFailure(call: Call<Void>, throwable: Throwable) {
                upAxisCallBack.onFailure(throwable)
            }

        })

    }


    /**
     * Method to track user action and provide info based on user information.
     */
    @JvmSuppressWildcards
    public fun eventCaptured(clickId: String, eventId: String, upAxisCallBack: UpAxisCallBack<Void>) {
        if (!validateUserData()) {
            upAxisCallBack.validationError("Validation Error")
            return
        }

        if (!context.isNetworkAvailable()) {
            upAxisCallBack.noNetworkAvailable()
            return
        }
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any>()
        queryParams[PARAM_K] = UpAxisConfig.AUTH_ID!!
        queryParams[PARAM_EVENT_ID] = eventId
        queryParams[PARAM_CLICK_ID] = clickId
        queryParams[PARAM_DUPLICATE] = 1
        val call = apiInterface?.postEvent(queryParams)
        call?.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                upAxisCallBack.onResponse(setUpResponseData(response))
            }

            override fun onFailure(call: Call<Void>, throwable: Throwable) {
                upAxisCallBack.onFailure(throwable)
            }

        })

    }


    /**
     * Method to track user action and provide info based on user information.
     */
    @JvmSuppressWildcards
    public fun sendExtraData(eventId: String, clickId: String, appType: String?, upAxisCallBack: UpAxisCallBack<Void>) {
        if (!validateUserData()) {
            upAxisCallBack.validationError("Validation Error")
            return
        }

        if (!context.isNetworkAvailable()) {
            upAxisCallBack.noNetworkAvailable()
            return
        }
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val queryParams = HashMap<String, Any>()
        queryParams[PARAM_K] = UpAxisConfig.AUTH_ID!!
        queryParams[PARAM_EVENT_ID] = eventId
        queryParams[PARAM_CLICK_ID] = clickId
        queryParams[PARAM_DUPLICATE] = 1
        queryParams[PARAM_DETAILS] = getDeviceData(appType)
        val call = apiInterface?.postEvent(queryParams)
        call?.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                upAxisCallBack.onResponse(setUpResponseData(response))
            }

            override fun onFailure(call: Call<Void>, throwable: Throwable) {
                upAxisCallBack.onFailure(throwable)
            }
        })

    }

    private fun validateUserData(): Boolean {
        if (UpAxisConfig.AUTH_ID.isNullOrEmpty() || UpAxisConfig.BASE_URL.isNullOrEmpty()) {
            return false
        }
        return true
    }

    private fun getDeviceData(appType: String?): JSONObject {
        val deviceData = JSONObject()
        deviceData.put("manufacturer", Build.MANUFACTURER)
        deviceData.put("model", Build.MODEL)
        deviceData.put("platform", "Android")
        deviceData.put("appType", if (appType.isNullOrEmpty()) "Game" else "App")
        return deviceData
    }

    private fun setUpResponseData(response: Response<Void>): UpAxisResponse {
        val upAxisResponse = UpAxisResponse()
        upAxisResponse.body = response.body()
        upAxisResponse.responseCode = response.code()
        upAxisResponse.isSuccessful = response.isSuccessful
        upAxisResponse.errorBody = response.errorBody().toString()
        upAxisResponse.message = response.message()
        return upAxisResponse
    }

}
