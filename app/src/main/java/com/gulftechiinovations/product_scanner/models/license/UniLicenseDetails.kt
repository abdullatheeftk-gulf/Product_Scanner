package com.gulftechiinovations.product_scanner.models.license

import kotlinx.serialization.Serializable

@Serializable
data class UniLicenseDetails(
    val licenseKey: String,
    val licenseType: String,
    val expiryDate: String?
)
