package com.scaletech.hsanalytic

import android.content.Context
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.scaletech.hsanalytic.apiservice.UpAxisCallBack
import com.scaletech.hsanalytic.model.UpAxisResponse
import com.scaletech.hsanalytic.pref.UpAxisPref
import com.scaletech.hsanalytic.utils.startUserTrackingService


class UpAxisConfig() {

    private lateinit var referrerClient: InstallReferrerClient
    private var context: Context? = null

    companion object {
        // Your server connection url
        internal var BASE_URL: String? = null

        // Application unique Auth id
        internal var AUTH_ID: String? = null

        // This flag will allow to post event with same event id.
        internal var ALLOW_DUPLICATE: Boolean = false

        // Flag to check either enable user tracking or not.
        internal var TRACK_USER: Boolean = false

        // User tracking interval in minutes. minimum it should be 5 minutes.
        internal var TRACK_INTERVAL: Int = 5

        // default track event name to log app session event
        internal var TRACK_EVENT_NAME: String = "session"
    }

    private constructor(builder: Builder) : this() {
        BASE_URL = builder.baseUrl
        AUTH_ID = builder.authId
        context = builder.context
        ALLOW_DUPLICATE = builder.duplicateEvent
        TRACK_INTERVAL = builder.interval
        TRACK_USER = builder.trackUser
        TRACK_EVENT_NAME = builder.trackEventName
        referrerClient = InstallReferrerClient.newBuilder(builder.context).build()
        if (TRACK_INTERVAL < 5) {
            throw java.lang.Exception("User tracking interval must be equal or more than 5 minutes")
        }
       context?.startUserTrackingService()
        setUpReferrerClient()
    }


    class Builder {
        internal var baseUrl: String = ""
        internal var authId: String = ""
        internal var context: Context? = null
        internal var duplicateEvent: Boolean = false
        internal var interval: Int = 5
        internal var trackUser:Boolean = false
        internal var trackEventName: String = "session"

        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }
        fun authId(authId: String) = apply { this.authId = authId }
        fun setContext(context: Context) = apply { this.context = context }
        fun setAllowDuplicate(duplicateEvent: Boolean) = apply { this.duplicateEvent = duplicateEvent }
        fun setTrackUser(trackUser: Boolean) = apply { this.trackUser = trackUser }
        fun setTrackInterval(interval: Int) = apply { this.interval = interval }
        fun setTrackEventName(trackEventName: String) = apply { this.trackEventName = trackEventName }
        fun build() = UpAxisConfig(this)

    }

    /**
     * method for referrer client connection
     */
    private fun setUpReferrerClient() {
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        if (response.referrerClickTimestampSeconds > 0) {
                            context?.let {
                                if (response.installReferrer.isNullOrEmpty())
                                    return
                                try {
                                    UpAxisPref.getInstance(it).setValue(UpAxisPref.REFERRAL, response.installReferrer.split("=")[1])
                                } catch (e: Exception) {
                                    UpAxisPref.getInstance(it).setValue(UpAxisPref.REFERRAL, "")
                                }
                                if (!UpAxisPref.getInstance(it).getValue(UpAxisPref.INSTALLED, false)) {
                                    UpAxis(it).postInstallEvent(object : UpAxisCallBack<Void> {
                                        override fun onResponse(upAxisResponse: UpAxisResponse) {
                                            UpAxisPref.getInstance(it).setValue(UpAxisPref.INSTALLED, true)
                                        }
                                        override fun onFailure(t: Throwable?) {}
                                        override fun noNetworkAvailable() {}
                                        override fun validationError(message: String) {}

                                    })
                                }
                            }
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