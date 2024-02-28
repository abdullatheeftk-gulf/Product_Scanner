package com.gulftechiinovations.product_scanner.screens.set_base_url_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulftechiinovations.product_scanner.data.api.ApiService
import com.gulftechiinovations.product_scanner.data.data_store.DataStoreService
import com.gulftechiinovations.product_scanner.data.firebase.FireStoreService
import com.gulftechiinovations.product_scanner.models.FirebaseError
import com.gulftechiinovations.product_scanner.models.GetDataFromRemote
import com.gulftechiinovations.product_scanner.navigation.RootNavScreens
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import com.gulftechiinovations.product_scanner.util.SharedMemory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetBaseUrlScreenViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedMemory:SharedMemory ,
    private val dataStoreService: DataStoreService,
    private val fireStoreService: FireStoreService
):ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setBaseUrl(baseUrl:String){
        sharedMemory.baseUrl = baseUrl
        getWelcomeMessage()
    }




    private fun getWelcomeMessage(){
        sendUiEvent(UiEvent.ShowProgressBar)
        viewModelScope.launch {
            apiService.getWelcomeMessage().collectLatest {result->
                when(result){
                    is GetDataFromRemote.Loading->{

                    }
                    is GetDataFromRemote.Success->{
                        sendUiEvent(UiEvent.CloseProgressBar)
                        // Saving base url on datastore
                        dataStoreService.saveBaseUrl(baseUrl = sharedMemory.baseUrl)
                        sendUiEvent(UiEvent.Navigate(RootNavScreens.HomeScreen.route))
                    }
                    is GetDataFromRemote.Failed->{
                        sendUiEvent(UiEvent.CloseProgressBar)
                        val error = result.error
                        val errorMessage = "${error.code}, ${error.message}"
                        sendUiEvent(UiEvent.ShowSnackBar(errorMessage))
                        sendUiEvent(UiEvent.ShowSetBaseUrlButton)
                        sendUiEvent(UiEvent.ShowSetBaseUrlScreenErrorMessage(error.message?:"There have problem while setting base url"))

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

    fun setErrorUrlValidationError(){
        sendUiEvent(UiEvent.ShowSetBaseUrlScreenErrorMessage("Check Entered url"))
    }



    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }


}