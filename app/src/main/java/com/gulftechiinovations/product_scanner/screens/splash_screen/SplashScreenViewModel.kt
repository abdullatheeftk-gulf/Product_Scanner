package com.gulftechiinovations.product_scanner.screens.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulftechiinovations.product_scanner.data.api.ApiService
import com.gulftechiinovations.product_scanner.data.data_store.DataStoreService
import com.gulftechiinovations.product_scanner.data.firebase.FireStoreService
import com.gulftechiinovations.product_scanner.models.FirebaseError
import com.gulftechiinovations.product_scanner.models.GetDataFromRemote
import com.gulftechiinovations.product_scanner.models.license.UniLicenseDetails
import com.gulftechiinovations.product_scanner.navigation.RootNavScreens
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import com.gulftechiinovations.product_scanner.util.SharedMemory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

//private const val TAG = "SplashScreenViewModel"

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val sharedMemory: SharedMemory,
    private val dataStoreService: DataStoreService,
    private val apiService: ApiService,
    private val fireStoreService: FireStoreService,
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    /*private val _uniLicenseDetails: MutableState<UniLicenseDetails?> = mutableStateOf(null)
    val uniLicenseDetails: State<UniLicenseDetails?> = _uniLicenseDetails*/



    init {

        getUniposLicenseDetailsFromDataStore()
        getIpAddressFromDataStore()


    }

    private fun getUniposLicenseDetailsFromDataStore() {
        viewModelScope.launch {
            dataStoreService.readUniLicenseData().collectLatest { license ->

                // Check for license information is available on data store
                if (license.isNotEmpty() && license.isNotBlank()) {

                    val licenseDetails = Json.decodeFromString<UniLicenseDetails>(license)
                    // _uniLicenseDetails.value = licenseDetails

                    // Check for license is Demo
                    if (licenseDetails.licenseType == "demo" && !licenseDetails.expiryDate.isNullOrBlank() && licenseDetails.expiryDate.isNotEmpty()) {

                        // check for license expired
                        if (isUniPosLicenseExpired(licenseDetails.expiryDate)) {
                            // demo license expired

                            sendUiEvent(UiEvent.ShowAlertDialog("Your License is Expired!"))


                        } else {
                            // demo license not expired

                            // get base url from the data store
                            getBaseUrlFromDataStoreAndSaveInSharedMemory()
                        }
                    }
                    if (licenseDetails.licenseType == "permanent") {
                        // License is permanent

                        // get base url from the data store
                        getBaseUrlFromDataStoreAndSaveInSharedMemory()

                    }
                } else {
                    // No license information on data store
                    sendUiEvent(UiEvent.ShowAlertDialog("Your app is not activated"))

                }
            }
        }
    }

    private fun getIpAddressFromDataStore() {
        viewModelScope.launch {
            dataStoreService.readIpaddress().collectLatest {
                if (it.isNotEmpty() || it.isNotBlank()) {
                    sharedMemory.ipAddress = it
                } else {
                    getIpAddressFromApi()
                }
            }
        }
    }


    private fun getIpAddressFromApi() {
        try {
            viewModelScope.launch {
                apiService.getIpAddress().collectLatest { value ->
                    when (value) {
                        is GetDataFromRemote.Loading -> {

                        }

                        is GetDataFromRemote.Success -> {


                            sharedMemory.ipAddress = value.data



                        }

                        is GetDataFromRemote.Failed -> {

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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBaseUrlFromDataStoreAndSaveInSharedMemory() {
        viewModelScope.launch {
            dataStoreService.readBaseUrl().collectLatest {
                //Log.w(TAG, "getBaseUrlAndSaveItOnSharedMemory: $it")

                if (it.isNotEmpty() || it.isNotBlank()) {
                    sharedMemory.baseUrl = it
                    getWelcomeMessage()
                    return@collectLatest
                }

                sendUiEvent(UiEvent.ShowSnackBar("Set your Base Url"))
                sendUiEvent(UiEvent.ShowSetBaseUrlButton)
            }
        }
    }


    fun onSetBaseUrlButtonClicked() {
        getFireBaseLicense {
            if (it) {
                sendUiEvent(UiEvent.Navigate(RootNavScreens.SetBaseUrlScreen.route))
            } else {
                sendUiEvent(UiEvent.ShowSnackBar("Some Error"))
            }
        }

    }

    private fun getWelcomeMessage() {
        viewModelScope.launch {
            apiService.getWelcomeMessage().collectLatest { result ->
                when (result) {
                    is GetDataFromRemote.Loading -> {
                        sendUiEvent(UiEvent.ShowProgressBar)
                    }

                    is GetDataFromRemote.Success -> {
                        sendUiEvent(UiEvent.CloseProgressBar)
                        //sendUiEvent(UiEvent.Navigate(RootNavScreens.HomeScreen.route))

                        getFireBaseLicense {
                            if (it) {
                                sendUiEvent(UiEvent.Navigate(RootNavScreens.HomeScreen.route))
                            } else {
                                sendUiEvent(UiEvent.ShowSnackBar("Some Error"))
                            }
                        }
                    }

                    is GetDataFromRemote.Failed -> {
                        sendUiEvent(UiEvent.CloseProgressBar)
                        val error = result.error
                        val errorMessage = "${error.code}, ${error.message}"
                        sendUiEvent(UiEvent.ShowSnackBar(errorMessage))
                        sendUiEvent(UiEvent.ShowSetBaseUrlButton)

                        val firebaseError = FirebaseError(
                            errorMessage = errorMessage,
                            errorCode = error.code,
                            ipAddress = sharedMemory.ipAddress
                        )
                        fireStoreService.addError(firebaseError)
                    }
                }
            }
        }
    }

    private fun getFireBaseLicense(callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            fireStoreService.getFirebaseLicense {
                callBack(it)
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }


    private fun isUniPosLicenseExpired(eDate: String): Boolean {

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

    fun navigateToLicenseActivationScreen() {
        sendUiEvent(UiEvent.Navigate(RootNavScreens.LicenseActivationScreen.route))
    }


}