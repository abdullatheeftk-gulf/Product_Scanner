package com.gulftechiinovations.product_scanner.screens.ui_util

sealed class UiEvent {

    data object ShowProgressBar : UiEvent()
    data object CloseProgressBar : UiEvent()
    data class ShowAlertDialog(val message:String) : UiEvent()
    data class ShowToastMessage(val message: String) : UiEvent()
    data class ShowSnackBar(val message: String, val action: (() -> Unit)? = null) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSetBaseUrlScreenErrorMessage(val errorMessage:String):UiEvent()
    data class ShowLicenceActivationScreenErrorMessage(val errorMessage: String):UiEvent()
    data object ShowSetBaseUrlButton : UiEvent()

}