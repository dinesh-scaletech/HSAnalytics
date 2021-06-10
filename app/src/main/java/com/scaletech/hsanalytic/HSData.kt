package com.scaletech.hsanalytic

import android.os.Build
import com.scaletech.hsanalytic.model.DeviceData

class HSData {
    public fun getDeviceData(): DeviceData {
        val deviceData = DeviceData()
        deviceData.manufacturer = Build.MANUFACTURER
        deviceData.model = Build.MODEL
        deviceData.platform = "Android"
        return deviceData
    }
}