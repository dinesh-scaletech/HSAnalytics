package com.scaletech.hsanalytic

import android.content.Context
import android.os.Build
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.scaletech.hsanalytic.apiservice.ApiInterface
import com.scaletech.hsanalytic.apiservice.ApiProvider
import com.scaletech.hsanalytic.apiservice.UpAxisCallBack
import com.scaletech.hsanalytic.model.UpAxisResponse
import com.scaletech.hsanalytic.pref.UpAxisPref
import com.scaletech.hsanalytic.utils.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


class UpAxis(private val context: Context) {

    private var adIdInfo: AdvertisingIdClient.Info? = null

    init {
        determineAdvertisingInfo()
    }

    /**
     * Common method to post data on the server.
     * @param eventId : String. Specifies what action the user performed.
     * it could be enum of integer(ex. 0,1,2..) in string or aliases(ex. reg,purchase,etc).
     * FOr example eventid = 0 or eventid = reg both are valid.
     * @param transactionId:String. Optional id of app owner.
     * @param receive : String. Monitoring value that should assigned to this conversion.
     * value should be in decimal format in string data type.
     * @param queue: Boolean. Either 0 (false) or 1(true), if 1 post backs will be queued and not handled in real time. default is 0
     * @param extraData:JSONObject. Any additional parameter can pass here in JSONObject.
     * @param upAxisCallBack Async call back inline function. here we used Void to get response in string.
     * for now are are not care about response from the server. we just need to post event.
     * while development process, we can check error or response data in application.
     */
    @JvmSuppressWildcards
    public fun postEvent(
        eventId: String?, transactionId: String ?= null, receive: String? = null,
        queue: Boolean = false, extraData: JSONObject = JSONObject(), upAxisCallBack: UpAxisCallBack<Void>? = null
    ) {
        val pair = validateUserData()
        if (!pair.first) {
            upAxisCallBack?.validationError(pair.second)
            return
        }
        if (!context.isNetworkAvailable()) {
            upAxisCallBack?.noNetworkAvailable()
            return
        }
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val bodyParams = HashMap<String, Any>()
        getCommonParameters(bodyParams)
        eventId?.let {
            bodyParams[PARAM_EVENT_ID] = it
        }
        transactionId?.let {
            bodyParams[PARAM_TRANSACTION_ID] = it
        }
        receive?.let {
            bodyParams[PARAM_RECEIVE] = it
        }

        bodyParams[PARAM_DETAILS] = getDeviceData(extraData)
        bodyParams[PARAM_B] = if (queue) 1 else 0

        val call = apiInterface?.postEvent(bodyParams)
        call?.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                upAxisCallBack?.onResponse(setUpResponseData(response))
            }

            override fun onFailure(call: Call<Void>, throwable: Throwable) {
                upAxisCallBack?.onFailure(throwable)
            }

        })
    }

    /**
     * function to post install app data to the server
     *
     * @param upAxisCallBack Async call back inline function. here we used Void to get response in string.
     * for now are are not care about response from the server. we just need to post event.
     * while development process, we can check error or response data in application.
     */
    @JvmSuppressWildcards
    fun postInstallEvent(upAxisCallBack: UpAxisCallBack<Void>? = null) {

        val pair = validateUserData()
        if (!pair.first) {
            upAxisCallBack?.validationError(pair.second)
            return
        }
        if (!context.isNetworkAvailable()) {
            upAxisCallBack?.noNetworkAvailable()
            return
        }
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val bodyParams = HashMap<String, Any>()
        getCommonParameters(bodyParams)
        bodyParams[PARAM_DETAILS] = getDeviceData(JSONObject())
        val call = apiInterface?.postEvent(bodyParams)
        call?.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                upAxisCallBack?.onResponse(setUpResponseData(response))
            }

            override fun onFailure(call: Call<Void>, throwable: Throwable) {
                upAxisCallBack?.onFailure(throwable)
            }

        })
    }


    /**
     * function to post user track event.
     * @param eventId : String. Specifies what action the user performed.
     * it could be enum of integer(ex. 0,1,2..) in string or aliases(ex. reg,purchase,etc).
     * FOr example eventid = 0 or eventid = reg both are valid.
     * @param upAxisCallBack Async call back inline function. here we used Void to get response in string.
     * for now are are not care about response from the server. we just need to post event.
     * while development process, we can check error or response data in application.
     */
    @JvmSuppressWildcards
    public fun postUserTrackEvent(eventId: String?, upAxisCallBack: UpAxisCallBack<Void>? = null) {
        val pair = validateUserData()
        if (!pair.first) {
            upAxisCallBack?.validationError(pair.second)
            return
        }
        if (!context.isNetworkAvailable()) {
            upAxisCallBack?.noNetworkAvailable()
            return
        }
        val apiInterface: ApiInterface? = ApiProvider.createServiceString()
        val bodyParams = HashMap<String, Any>()
        getCommonParameters(bodyParams, true)
        eventId?.let {
            bodyParams[PARAM_EVENT_ID] = it
        }

        bodyParams[PARAM_DETAILS] = getDeviceData(JSONObject())

        val call = apiInterface?.postEvent(bodyParams)
        call?.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                upAxisCallBack?.onResponse(setUpResponseData(response))
            }

            override fun onFailure(call: Call<Void>, throwable: Throwable) {
                upAxisCallBack?.onFailure(throwable)
            }

        })
    }

    // todo temp function to test events without installed from play store.
    public fun saveReferral(referralCode: String) {
        UpAxisPref.getInstance(context).setValue(UpAxisPref.REFERRAL, referralCode)
    }

    /**
     * Method to check user inputs. if valid data is not passed then fun will return appropriate message with boolean
     */
    private fun validateUserData(): Pair<Boolean, String> {
        if (UpAxisConfig.AUTH_ID.isNullOrEmpty()) {
            return Pair(false, "Auth Id is null or empty")
        }

        if (UpAxisConfig.BASE_URL.isNullOrEmpty()) {
            return Pair(false, "BaseUrl is null or empty")
        }

        if (UpAxisPref.getInstance(context).getValue(UpAxisPref.REFERRAL, "").isEmpty()) {
            return Pair(false, "Referral is empty")
        }
        return Pair(true, "") // If any of if condition does not match then we pass empty message and just ignore in result section.
    }

    /**
     * Function to add more data along with user data.
     */
    private fun getDeviceData(extraData: JSONObject): JSONObject {
        extraData.put("manufacturer", Build.MANUFACTURER)
        extraData.put("model", Build.MODEL)
        extraData.put("platform", "Android")
        if (adIdInfo != null) {
            extraData.put("advertisementId", adIdInfo?.id)
            extraData.put("packageName", adIdInfo?.isLimitAdTrackingEnabled)
        }
        extraData.put("libVersion", System.getenv("VERSION_NAME"))
        return extraData
    }

    /**
     * Common function to add required parameters that are set from configurable part.
     */
    private fun getCommonParameters(bodyParams: HashMap<String, Any>, allowBackup: Boolean = false): HashMap<String, Any> {
        bodyParams[PARAM_K] = UpAxisConfig.AUTH_ID!!
        bodyParams[PARAM_CLICK_ID] = UpAxisPref.getInstance(context).getValue(UpAxisPref.REFERRAL, "")
        bodyParams[PARAM_DUPLICATE] = if (allowBackup) {
            true
        } else {
            if (UpAxisConfig.ALLOW_DUPLICATE) 1 else 0
        }
        return bodyParams
    }

    /**
     * Function to add more data in response object that will be passed to the application.
     */
    private fun setUpResponseData(response: Response<Void>): UpAxisResponse {
        val upAxisResponse = UpAxisResponse()
        upAxisResponse.body = response.body()
        upAxisResponse.responseCode = response.code()
        upAxisResponse.isSuccessful = response.isSuccessful
        upAxisResponse.errorBody = response.errorBody().toString()
        upAxisResponse.message = response.message()
        return upAxisResponse
    }

    /**
     * Function to start service if it is not running
     */
    public fun startUserTrackingService() {
        context.startUserTrackingService()
    }

    /**
     * Function to stop user tracking and stop running service.
     */
    public fun stopUserTrackingService() {
        context.stopUserTrackingService()
    }

    private fun determineAdvertisingInfo() {
        if (adIdInfo != null) {
            return
        }
        Thread {
            try {
                val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
                // You should check this in case the user disabled it from settings
                if (!advertisingIdInfo.isLimitAdTrackingEnabled) {
                    adIdInfo = advertisingIdInfo
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            }
        }.start()

    }

}
