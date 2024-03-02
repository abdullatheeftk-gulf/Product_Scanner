package com.gulftechiinovations.product_scanner.models


import androidx.annotation.Keep
import java.util.Date

@Keep
data class DeviceData(
    val manufacturerName:String?= android.os.Build.MANUFACTURER,
    val versionName:String = "V01-02-03-2024 -actual",
    val model: String? = android.os.Build.MODEL,
    val device: String? = android.os.Build.DEVICE,
    val date:String = Date().toString(),
    val ipv4Address: String?,
    val deviceId:String? = null,
    val screenSize:DeviceSize?
)
