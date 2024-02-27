package com.gulftechiinovations.product_scanner.screens.license_activation_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gulftechiinovations.product_scanner.screens.license_activation_screen.componenets.LicenseActivationScreenHeading
import com.gulftechiinovations.product_scanner.screens.license_activation_screen.componenets.LicenseDetailsAlertDialog
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LicenseActivationScreen(
    licenseActivationScreenViewModel:LicenseActivationScreenViewModel = hiltViewModel(),
    hideKeyboard:()->Unit,
    navHostController: NavHostController
) {
    var showProgressBar by remember {
        mutableStateOf(false)
    }

    var showAlertDialog by remember {
        mutableStateOf(false)
    }

    var alertDialogMessage by remember {
        mutableStateOf("")
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    var licenseText by remember {
        mutableStateOf("")
    }

    var showError by remember {
        mutableStateOf(false)
    }

    var errorMessage:String? by remember{
        mutableStateOf(null)
    }

    val focusRequester by remember {
        mutableStateOf(FocusRequester())
    }

    LaunchedEffect(true){
        focusRequester.requestFocus()
        licenseActivationScreenViewModel.uiEvent.collectLatest {value ->
            when(value){
                is UiEvent.ShowProgressBar->{
                    showProgressBar = true
                }
                is UiEvent.CloseProgressBar->{
                    showProgressBar = false
                }
                is UiEvent.ShowSnackBar->{
                    snackBarHostState.showSnackbar(value.message)
                }
                is UiEvent.ShowAlertDialog->{
                    showAlertDialog= true
                    alertDialogMessage = value.message
                }
                is UiEvent.Navigate->{
                    navHostController.popBackStack()
                    navHostController.navigate(value.route)
                }
                is UiEvent.ShowLicenceActivationScreenErrorMessage->{
                    errorMessage = value.errorMessage
                    showError = true
                }
                else->Unit
            }
        }
    }
    
    if(showAlertDialog){
        LicenseDetailsAlertDialog(
            alertDialogMessage = alertDialogMessage,
            onDismissRequest = { 
                showAlertDialog = false
                alertDialogMessage = ""
            }
        ) {
            showAlertDialog = false
            alertDialogMessage = ""
            licenseActivationScreenViewModel.getWelcomeMessage()
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        
    ){
        it.calculateTopPadding()
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LicenseActivationScreenHeading()
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(

                value = licenseText,
                onValueChange = { typedText ->
                    licenseText = typedText
                    showError = false
                    errorMessage = null
                },
                placeholder = {
                    Text(
                        text = "Enter License Key",
                        //fontSize = if(screenWidth<600f) 14.sp else if(screenWidth>=600 && screenWidth<900) 18.sp else 22.sp
                        fontSize = 42.sp
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboard()
                        licenseActivationScreenViewModel.uniposLicenseActivation(licenseKey = licenseText)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester = focusRequester)
                    .padding(vertical = it.calculateTopPadding(), horizontal = 32.dp),
                enabled = !showProgressBar,
                textStyle = TextStyle(
                    fontSize = 42.sp
                    //fontSize = if (screenWidth < 600f) 14.sp else if (screenWidth >= 600 && screenWidth < 800) 18.sp else 22.sp,
                ),
                isError = showError,
                supportingText = {
                    if (errorMessage!=null) {
                        Text(text = errorMessage ?:"", fontSize = 28.sp, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    hideKeyboard()
                    if(licenseKeyValidation(licenseText)) {
                        licenseActivationScreenViewModel.uniposLicenseActivation(licenseKey = licenseText)
                    }
                    else{
                        errorMessage = "invalid License key"
                        showError = true
                    }
                },
                enabled = !showProgressBar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00952E),
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Black
                )
            ) {
                Text(
                    text = "Activate",
                    fontSize = 42.sp,
                    modifier = Modifier.padding(16.dp)
                    // fontSize = if(screenWidth<600f) 14.sp else if(screenWidth>=600 && screenWidth<800) 18.sp else 22.sp
                )

            }

            if(showProgressBar){
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressIndicator()
            }
        }

    }
}

private fun licenseKeyValidation(value: String): Boolean {
    return value.startsWith('C', ignoreCase = false)
}