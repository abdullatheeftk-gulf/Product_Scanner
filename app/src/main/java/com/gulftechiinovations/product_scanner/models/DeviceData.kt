package com.gulftechiinovations.product_scanner.models


import java.util.Date


data class DeviceData(
    val manufacturerName:String?= android.os.Build.MANUFACTURER,
    val model: String? = android.os.Build.MODEL,
    val device: String? = android.os.Build.DEVICE,
    val date:String = Date().toString(),
    val ipv4Address: String?,
    val deviceId:String? = null,
    val screenSize:DeviceSize?
)
