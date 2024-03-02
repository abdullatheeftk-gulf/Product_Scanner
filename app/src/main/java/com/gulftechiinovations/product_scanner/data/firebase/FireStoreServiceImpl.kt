package com.gulftechiinovations.product_scanner.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.gulftechiinovations.product_scanner.models.DeviceData
import com.gulftechiinovations.product_scanner.models.FirebaseError
import java.util.Date

private const val TAG = "FireStoreServiceImpl"

class FireStoreServiceImpl(
    private val fireStoreDb: FirebaseFirestore
) : FireStoreService {
    override suspend fun addData(deviceData: DeviceData) {
        Log.d(TAG, "addData: $deviceData")
        fireStoreDb.collection("DeviceData").document(Date().toString()).set(deviceData)
            .addOnSuccessListener {
                Log.i(TAG, "addData: success")
            }.addOnFailureListener {
            Log.e(TAG, "addData: $it")
        }.addOnCompleteListener {
                Log.e(TAG, "addData: completed", )
        }
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