package com.gulftechiinovations.product_scanner.screens.license_activation_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulftechiinovations.product_scanner.data.api.ApiService
import com.gulftechiinovations.product_scanner.data.data_store.DataStoreService
import com.gulftechiinovations.product_scanner.data.firebase.FireStoreService
import com.gulftechiinovations.product_scanner.models.FirebaseError
import com.gulftechiinovations.product_scanner.models.GetDataFromRemote
import com.gulftechiinovations.product_scanner.models.license.LicenseRequestBody
import com.gulftechiinovations.product_scanner.models.license.UniLicenseDetails
import com.gulftechiinovations.product_scanner.navigation.RootNavScreens
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import com.gulftechiinovations.product_scanner.util.SharedMemory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

//private const val TAG = "LicenseActivationScreen"
@HiltViewModel
class LicenseActivationScreenViewModel @Inject constructor(
    private val sharedMemory:SharedMemory,
    private val apiService: ApiService,
    private val dataStoreService: DataStoreService,
    private val deviceId:String,
    private val fireStoreService: FireStoreService
):ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var _ipAddress :String?= null
    private var _deviceId:String? = null

    init {
        _ipAddress = sharedMemory.ipAddress
        if(_ipAddress==null){
            getIpAddressFromApi()
        }
        _deviceId = sharedMemory.deviceId

        if(_deviceId == null){
            readDeviceIdFromDataStore()
        }
       // Log.e(TAG, "$deviceId: ", )

    }

    private fun readDeviceIdFromDataStore(){
        viewModelScope.launch {
            dataStoreService.readDeviceId().collectLatest {
                if(it.isEmpty() || it.isBlank()){
                    sharedMemory.deviceId = deviceId
                    _deviceId = deviceId
                }else{
                    sharedMemory.deviceId = it
                    _deviceId=it
                }
            }
        }
    }

    private fun getIpAddressFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getIpAddress().collectLatest {value->
                when(value){
                    is GetDataFromRemote.Loading->{

                    }
                    is GetDataFromRemote.Success->{
                        sharedMemory.ipAddress = value.data
                        _ipAddress = value.data


                    }
                    is GetDataFromRemote.Failed->{
                        sharedMemory.ipAddress = null
                        delay(1000)
                        val errorCode = value.error.code
                        if (errorCode in 500..700) {
                            getIpAddressFromApi()
                        }
                    }
                }

            }
        }
    }

    fun uniposLicenseActivation(licenseKey:String){
        sendUiEvent(UiEvent.ShowProgressBar)
        if(_ipAddress == null){
            sendUiEvent(UiEvent.ShowSnackBar("Please wait!. Ip address is loading or check Internet"))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val licenseRequestBody =
                LicenseRequestBody(
                    licenseKey = licenseKey,
                    macId = _deviceId!!,
                    ipAddress = _ipAddress!!
                )

            apiService.uniLicenseActivation(licenseRequestBody = licenseRequestBody).collectLatest {value ->
                when(value){
                    is GetDataFromRemote.Loading->{

                    }
                    is GetDataFromRemote.Success->{
                        sendUiEvent(UiEvent.ShowProgressBar)
                        val licenseResponse = value.data
                        if(licenseResponse.licenseType == "permanent"){

                            // Permanent license
                            val uniLicenseDetails = UniLicenseDetails(
                                licenseKey = licenseKey,
                                licenseType = licenseResponse.licenseType,
                                expiryDate = licenseResponse.expiryDate
                            )
                            sharedMemory.uniLicenseDetails = uniLicenseDetails

                            val uniLicenseDetailsString = Json.encodeToString<UniLicenseDetails>(uniLicenseDetails)
                            //Log.e(TAG, "uniposLicenseActivation: $uniLicenseDetailsString", )
                            sendUiEvent(UiEvent.ShowAlertDialog(message = uniLicenseDetailsString))


                        }
                        if(licenseResponse.licenseType == "demo"){
                            // Demo license
                            val expiryDate = licenseResponse.expiryDate
                            expiryDate?.let {
                                if(isUniPosLicenseExpired(it)){
                                    // License Expired
                                    sendUiEvent(UiEvent.CloseProgressBar)
                                    sendUiEvent(UiEvent.ShowSnackBar("$licenseKey is expired, Please enter another license"))


                                }else{
                                    // License is not expired
                                    val uniLicenseDetails = UniLicenseDetails(
                                        licenseKey = licenseKey,
                                        licenseType = licenseResponse.licenseType,
                                        expiryDate = licenseResponse.expiryDate
                                    )
                                    sharedMemory.uniLicenseDetails = uniLicenseDetails

                                    val uniLicenseDetailsString = Json.encodeToString<UniLicenseDetails>(uniLicenseDetails)
                                   // Log.e(TAG, "uniposLicenseActivation: $uniLicenseDetailsString", )
                                    sendUiEvent(UiEvent.ShowAlertDialog(message = uniLicenseDetailsString))

                                }
                            }

                        }

                    }
                    is GetDataFromRemote.Failed->{
                        sendUiEvent(UiEvent.CloseProgressBar)
                        val error = value.error
                        val errorMessage= error.message
                        sendUiEvent(UiEvent.ShowSnackBar(errorMessage?:"There have some problem while registering license"))
                        sendUiEvent(UiEvent.ShowLicenceActivationScreenErrorMessage(errorMessage?:"There have some problem while registering license"))

                        val firebaseError = FirebaseError(
                            errorMessage = errorMessage ?:"There have some problem while registering license",
                            errorCode = error.code,
                            ipAddress = sharedMemory.ipAddress
                        )

                        fireStoreService.addError(firebaseError)

                    }
                }

            }
        }
    }


     fun getWelcomeMessage() {
         sendUiEvent(UiEvent.ShowProgressBar)
        viewModelScope.launch (Dispatchers.IO){
            apiService.getWelcomeMessage().collectLatest { result ->
                when (result) {
                    is GetDataFromRemote.Loading -> {

                    }

                    is GetDataFromRemote.Success -> {
                        sendUiEvent(UiEvent.CloseProgressBar)
                        sendUiEvent(UiEvent.Navigate(RootNavScreens.HomeScreen.route))
                    }

                    is GetDataFromRemote.Failed -> {
                        sendUiEvent(UiEvent.CloseProgressBar)
                        val error = result.error
                        val errorMessage = "${error.code}, ${error.message}"
                        sendUiEvent(UiEvent.ShowSnackBar(errorMessage))
                        sendUiEvent(UiEvent.Navigate(RootNavScreens.SetBaseUrlScreen.route))

                        val firebaseError = FirebaseError(
                            errorMessage = errorMessage ,
                            errorCode = error.code,
                            ipAddress = sharedMemory.ipAddress
                        )

                        fireStoreService.addError(firebaseError)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }


    private fun isUniPosLicenseExpired(eDate: String): Boolean {
        if(eDate.isEmpty() || eDate.isBlank()){
            return  true
        }

        return try {
            val expDate: Date = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).parse(eDate)!!

            val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
            val month = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
            val day = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

            val currentDate =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("${day}-${month}-${year}")


            val comparison = currentDate?.after(expDate)!!


            comparison
        } catch (e: Exception) {
            true
        }
    }




}