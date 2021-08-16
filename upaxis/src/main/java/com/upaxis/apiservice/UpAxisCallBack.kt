package com.upaxis.apiservice

import com.upaxis.model.UpAxisResponse
import retrofit2.Response

interface UpAxisCallBack<T> {
        /**
         * Invoked for a received HTTP response.
         *
         *
         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
         * Call [Response.isSuccessful] to determine if the response indicates success.
         */
        fun onResponse(upAxisResponse: UpAxisResponse)

        /**
         * Invoked when a network exception occurred talking to the server or when an unexpected exception
         * occurred creating the request or processing the response.
         */
        fun onFailure(t: Throwable?)

        fun noNetworkAvailable()
        fun validationError(message:String)
}