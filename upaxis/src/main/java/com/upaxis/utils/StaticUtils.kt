package com.upaxis.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.upaxis.UpAxisConfig
import com.upaxis.service.TrackingService


internal fun Context.isNetworkAvailable(): Boolean {

    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

}


fun Context.startUserTrackingService() {
    // If trackUser is false then do not start service or post event...
    if (!UpAxisConfig.TRACK_USER) {
        if (isServiceRunning(TrackingService::class.java)) {
            stopUserTrackingService()
        }
        return
    }
    if (!isServiceRunning(TrackingService::class.java)) {
        val serviceIntent = Intent(this, TrackingService::class.java)
        startService(serviceIntent)
    }
}


fun Context.stopUserTrackingService() {
    if (isServiceRunning(TrackingService::class.java)) {
        val serviceIntent = Intent(this, TrackingService::class.java)
        stopService(serviceIntent)
    }

}

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services: List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServices(Int.MAX_VALUE)
    for (runningServiceInfo in services) {
        return serviceClass.name == runningServiceInfo.service.className
    }
    return false
}