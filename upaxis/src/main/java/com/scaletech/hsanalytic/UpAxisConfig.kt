package com.scaletech.hsanalytic

import android.content.Context
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.scaletech.hsanalytic.pref.UpAxisPref


public class UpAxisConfig() {

    private lateinit var referrerClient: InstallReferrerClient
    private var context: Context? = null

    companion object {
        internal var BASE_URL: String? = null
        internal var AUTH_ID: String? = null
    }

    private constructor(builder: Builder) : this() {
        BASE_URL = builder.baseUrl
        AUTH_ID = builder.authId
        context = builder.context
        referrerClient = InstallReferrerClient.newBuilder(builder.context).build()
        setUpReferrerClient()
    }

    class Builder {
        var baseUrl: String = ""
            private set
        var authId: String = ""
            private set

        var context: Context? = null
            private set

        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        fun authId(authId: String) = apply { this.authId = authId }
        fun setContext(context: Context) = apply { this.context = context }
        fun build() = UpAxisConfig(this)

    }

    /*public fun validateConfigs(){
        if (baseUrl.isEmpty()){
            return
        }
    }*/

    /**
     * method for referrer client connection
     */
    private fun setUpReferrerClient() {
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        if (!response.referrerClickTimestampSeconds > 0) {
                            Log.e("ReferralCode", response.installReferrer)
                            Log.e("referrerClickTimestampSeconds", response.referrerClickTimestampSeconds.toString())
                            context?.let { UpAxisPref.getInstance(it).setValue(UpAxisPref.REFERRAL, response.installReferrer) }
                        }
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                        Log.e(
                            "ReferrerClient : ",
                            "API not available for current play store app"
                        )
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        Log.e("ReferrerClient : ", "Connection could'nt Established")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
            }
        })
    }

}