package com.gulftechiinovations.product_scanner.models

import androidx.annotation.Keep
import java.util.Date

@Keep
data class FirebaseError(
    val errorMessage:String,
    val errorCode:Int,
    val date:String = Date().toString(),
    val ipAddress:String?=null
)
