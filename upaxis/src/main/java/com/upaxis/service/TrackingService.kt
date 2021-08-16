package com.upaxis.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.upaxis.UpAxis
import com.upaxis.UpAxisConfig
import com.upaxis.UpAxisConfig.Companion.TRACK_INTERVAL
import com.upaxis.apiservice.UpAxisCallBack
import com.upaxis.model.UpAxisResponse
import kotlinx.coroutines.*

class TrackingService : Service() {

    private var upAxis: UpAxis? = null
    private var job: Job? = null

    @InternalCoroutinesApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        upAxis = UpAxis(this)
        startUserTracking()
        return START_NOT_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @InternalCoroutinesApi
    private fun startUserTracking() {
        job = startRepeatingJob()
    }

    @InternalCoroutinesApi
    private fun startRepeatingJob(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            while (NonCancellable.isActive) {
                updateServer()
                delay(TRACK_INTERVAL.toLong())
            }
        }
    }

    private fun updateServer() {

        if (!UpAxisConfig.TRACK_USER) {
            stopJob()
            stopSelf()
        }
        upAxis?.postUserTrackEvent(eventId = UpAxisConfig.TRACK_EVENT_NAME, upAxisCallBack = object : UpAxisCallBack<Void> {
            override fun onResponse(upAxisResponse: UpAxisResponse) {
                Log.e("Track User onResponse", "Status::{${upAxisResponse.isSuccessful}}")
            }

            override fun onFailure(t: Throwable?) {
                Log.e("onFailure", "message::{${t?.message}}")
            }

            override fun noNetworkAvailable() {

            }

            override fun validationError(message: String) {

            }

        })
    }

    /**
     * Function to start tracking by starting job.
     */
    fun startTracking() {
        job?.start()
    }

    /**
     * Function to stop tracking by cancelling ongoing job.
     */
    private fun stopJob() {
        job?.cancel()
    }

}