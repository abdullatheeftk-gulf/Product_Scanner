package com.gulftechiinovations.product_scanner.data.api

import com.gulftechiinovations.product_scanner.models.CompanyData
import com.gulftechiinovations.product_scanner.models.GetDataFromRemote
import com.gulftechiinovations.product_scanner.models.Product
import com.gulftechiinovations.product_scanner.models.WelcomeMessage
import com.gulftechiinovations.product_scanner.models.license.LicenseRequestBody
import com.gulftechiinovations.product_scanner.models.license.LicenseResponse
import kotlinx.coroutines.flow.Flow

interface ApiService {

    suspend fun getWelcomeMessage():Flow<GetDataFromRemote<WelcomeMessage>>

    suspend fun getCompanyName():Flow<GetDataFromRemote<CompanyData>>

    suspend fun getProductByBarcode(barcode:String): Flow<GetDataFromRemote<Product?>>

    suspend fun getIpAddress():Flow<GetDataFromRemote<String?>>

    suspend fun getCompanyLogo():Flow<GetDataFromRemote<ByteArray?>>

    suspend fun uniLicenseActivation(licenseRequestBody: LicenseRequestBody):Flow<GetDataFromRemote<LicenseResponse>>

}