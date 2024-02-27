package com.gulftechiinovations.product_scanner.models.license

import kotlinx.serialization.Serializable

@Serializable
data class LicenseRequestBody(
    val licenseKey: String,
    val macId: String,
    val ipAddress: String,
)
