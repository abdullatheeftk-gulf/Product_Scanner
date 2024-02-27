package com.gulftechiinovations.product_scanner.util

import com.gulftechiinovations.product_scanner.models.license.UniLicenseDetails


object SharedMemory {
    var baseUrl = ""
    var ipAddress:String? = null
    var deviceId:String? = null
    var uniLicenseDetails:UniLicenseDetails? = null
}