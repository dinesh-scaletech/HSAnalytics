package com.scaletech.hsanalytic

import android.os.Build
import org.json.JSONObject

class HSData {
    public fun getDeviceData(): JSONObject {
        val deviceData = JSONObject()
        deviceData.put("manufacturer", Build.MANUFACTURER)
        deviceData.put("model", Build.MODEL)
        deviceData.put("platform", "Android")
        return deviceData
    }
}