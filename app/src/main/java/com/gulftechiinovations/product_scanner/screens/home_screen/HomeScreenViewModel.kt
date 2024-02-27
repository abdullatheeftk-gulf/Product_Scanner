package com.gulftechiinovations.product_scanner.screens.home_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulftechiinovations.product_scanner.data.api.ApiService
import com.gulftechiinovations.product_scanner.data.data_store.DataStoreService
import com.gulftechiinovations.product_scanner.data.firebase.FireStoreService
import com.gulftechiinovations.product_scanner.models.DeviceData
import com.gulftechiinovations.product_scanner.models.DeviceSize
import com.gulftechiinovations.product_scanner.models.GetDataFromRemote
import com.gulftechiinovations.product_scanner.models.Product
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import com.gulftechiinovations.product_scanner.util.Constants
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
import javax.inject.Inject

private const val TAG = "HomeScreenViewModel"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val apiService: ApiService,
    private val fireStoreService: FireStoreService,
    private val sharedMemory:SharedMemory,
    private val dataStoreService: DataStoreService
):ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private  val _productState:MutableState<Product?> = mutableStateOf(null)
    val productState:State<Product?> = _productState

    private val _barcode:MutableState<String> = mutableStateOf("")
    val barcode:State<String> = _barcode

    private val _scannedBarcode:MutableState<String> = mutableStateOf("")
    val scannedBarcode:State<String> = _scannedBarcode

    

    private var _delayTimerToShowLogo:Int= Constants.IDLE_TIME_VALUE

    private val _showIdleLogo:MutableState<Boolean> = mutableStateOf(false)
    val showIdleLogo:State<Boolean> = _showIdleLogo

    private val _baseUrl:MutableState<String> = mutableStateOf("")
    val baseUrl:State<String> =_baseUrl

    private val _companyName:MutableState<String?> = mutableStateOf(null)
    val companyName:State<String?> = _companyName

    private val _companyLogo:MutableState<ByteArray?> = mutableStateOf(null)
    val companyLogo:State<ByteArray?> = _companyLogo



    

    // Scanning
    fun setBarcode(value:String){
        _showIdleLogo.value= false
        _barcode.value = value
    }


    // Barcode showing
    private fun setScannedBarcode(value: String){
        _scannedBarcode.value = value
    }

    init {

        saveUniLicenseDetailsOnDataStore()
        saveDeviceIdOnDataStore()
        saveIpAddressOnDataStore()


        
        setBaseUrl()
        getCompanyName()
        getCompanyLogo()
        startTimer()

    }

    private fun getCompanyLogo() {
        viewModelScope.launch (Dispatchers.IO){
            apiService.getCompanyLogo().collectLatest {value->
                when(value){

                    is GetDataFromRemote.Loading->{

                    }
                    is GetDataFromRemote.Success->{
                       _companyLogo.value =  value.data
                    }
                    is GetDataFromRemote.Failed->{

                    }
                }

            }
        }
    }

    private fun saveUniLicenseDetailsOnDataStore(){
        viewModelScope.launch {
            dataStoreService.readUniLicenseData().collectLatest {
                if(it.isEmpty()||it.isBlank()){
                    sharedMemory.uniLicenseDetails?.let {uniLicenseDetails ->
                        val uniLicenseDetailsString = Json.encodeToString(uniLicenseDetails)
                        dataStoreService.saveUniLicenseData(uniLicenseDetailsString)
                    }
                }
            }

        }
    }

    private fun saveDeviceIdOnDataStore(){
        viewModelScope.launch {
            dataStoreService.readDeviceId().collectLatest {
                if(it.isEmpty()||it.isBlank()){
                    sharedMemory.deviceId?.let{deviceId->
                        dataStoreService.saveDeviceId(deviceId)
                    }
                }
            }
        }
    }
    private fun saveIpAddressOnDataStore(){
        viewModelScope.launch {
            dataStoreService.readIpaddress().collectLatest {
                if(it.isEmpty()||it.isBlank()){
                    sharedMemory.deviceId?.let {ipAddress->
                        dataStoreService.saveIpAddress(ipAddress)
                    }
                }
            }
        }
    }

    private fun getCompanyName(){
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getCompanyName().collectLatest {value ->
                when(value){
                    is GetDataFromRemote.Loading->{

                    }
                    is GetDataFromRemote.Success->{
                        val company = value.data.companyName
                        _companyName.value = company
                        Log.d(TAG, "getCompanyName: $company")

                    }
                    is GetDataFromRemote.Failed->{
                        val error = value.error
                        val errorMessage = "${error.code}, ${error.message}"
                        sendUiEvent(UiEvent.ShowSnackBar(message = errorMessage))
                    }
                }

            }
        }
    }

    private fun setBaseUrl(){
        _baseUrl.value = sharedMemory.baseUrl
    }

    fun getProduct(barcode:String){
        setScannedBarcode(barcode)
        sendUiEvent(UiEvent.ShowProgressBar)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                /*val randomNumber = Random.nextInt(1,6)
                _productState.value = apiService.getProductByBarcode(barcode = barcode, randomNumber)*/
                apiService.getProductByBarcode(barcode).collectLatest {value->
                    when(value){
                        is GetDataFromRemote.Loading->{

                        }
                        is GetDataFromRemote.Success->{
                            startTimer()
                            _productState.value = value.data
                            sendUiEvent(UiEvent.CloseProgressBar)

                        }
                        is GetDataFromRemote.Failed->{
                            sendUiEvent(UiEvent.CloseProgressBar)
                            val error = value.error
                            val errorMessage = "${error.code}, ${error.message}"
                            sendUiEvent(UiEvent.ShowSnackBar(message = errorMessage))
                            _productState.value =null
                            startTimer()
                        }
                    }

                }

            }catch (e:Exception){
                _productState.value = null
                e.printStackTrace()
            }

        }

    }

    private fun startTimer(){
        _delayTimerToShowLogo = Constants.IDLE_TIME_VALUE
        viewModelScope.launch {
            repeat((1..Constants.IDLE_TIME_VALUE).count()) {
                delay(1000L)
                _delayTimerToShowLogo -= 1
                if(_delayTimerToShowLogo==0){
                    _productState.value = null
                    _showIdleLogo.value = true
                }
            }
        }
    }



    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }



}