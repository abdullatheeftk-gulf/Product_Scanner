package com.gulftechiinovations.product_scanner.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.gulftechiinovations.product_scanner.models.DeviceData
import com.gulftechiinovations.product_scanner.models.FirebaseError

//private const val TAG = "FireStoreServiceImpl"

class FireStoreServiceImpl(
    private val fireStoreDb: FirebaseFirestore
) : FireStoreService {
    override suspend fun addData(deviceData: DeviceData) {
        fireStoreDb.collection("DeviceData").add(deviceData)
    }

    override suspend fun getFirebaseLicense(callBack: (Boolean) -> Unit) {
        fireStoreDb.collection("license")
            .document("scanner")
            .get()
            .addOnSuccessListener {
                val result = it.get("value") as Boolean
                callBack(result)
            }.addOnFailureListener {
                callBack(false)
            }
    }

    override suspend fun addError(firebaseError: FirebaseError) {
        fireStoreDb.collection("error").add(firebaseError)
    }
}