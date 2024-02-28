package com.gulftechiinovations.product_scanner.data.firebase

import com.gulftechiinovations.product_scanner.models.DeviceData
import com.gulftechiinovations.product_scanner.models.FirebaseError

interface FireStoreService {
    suspend fun addData(deviceData: DeviceData)
    suspend fun getFirebaseLicense(callBack:(Boolean)->Unit)
    suspend fun addError(firebaseError: FirebaseError)
}