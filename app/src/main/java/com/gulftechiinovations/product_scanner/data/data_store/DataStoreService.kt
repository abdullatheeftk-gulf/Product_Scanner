package com.gulftechiinovations.product_scanner.data.data_store

import kotlinx.coroutines.flow.Flow

interface DataStoreService {

    // Write operation
    suspend fun updateOperationCount()
    suspend fun saveBaseUrl(baseUrl:String)
    suspend fun saveIpAddress(ipAddress: String)
    suspend fun saveUniLicenseData(uniLicenseString: String)
    suspend fun saveDeviceId(deviceId:String)


    // Read operation
    fun readOperationCount(): Flow<Int>
    fun readBaseUrl(): Flow<String>
    fun readIpaddress(): Flow<String>
    fun readUniLicenseData(): Flow<String>
    fun readDeviceId(): Flow<String>

}