package com.gulftechiinovations.product_scanner.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val productName:String,
    val productLocalName:String?,
    val productPrice:Float,
)
