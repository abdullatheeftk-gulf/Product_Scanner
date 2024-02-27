package com.gulftechiinovations.product_scanner.screens.set_base_url_screen

import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
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
import androidx.compose.material3.SnackbarDuration
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
import com.gulftechiinovations.product_scanner.screens.set_base_url_screen.componenets.SetBaseUrlHeadingSection
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "SetBaseUrlScreen"
@Composable
fun SetBaseUrlScreen(
    navHostController: NavHostController,
    hideKeyboard: () -> Unit,
    setBaseUrlScreenViewModel: SetBaseUrlScreenViewModel = hiltViewModel<SetBaseUrlScreenViewModel>()
) {

    var showError  by remember {
        mutableStateOf(false)
    }

    var errorMessage:String? by remember {
        mutableStateOf(null)
    }

    var showProgressBar by remember {
        mutableStateOf(false)
    }

    var baseUrlText by remember {
        mutableStateOf("")
    }

    val focusRequester by remember {
        mutableStateOf(FocusRequester())
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Log.d(TAG, "SetBaseUrlScreen: $showProgressBar")


    LaunchedEffect(true) {
        focusRequester.requestFocus()
        setBaseUrlScreenViewModel.uiEvent.collectLatest { value: UiEvent ->
            when (value) {
                is UiEvent.ShowProgressBar -> {
                    showProgressBar = true
                }

                is UiEvent.CloseProgressBar -> {
                    showProgressBar = false

                }

                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(value.message, duration = SnackbarDuration.Long)
                }

                is UiEvent.Navigate -> {
                    navHostController.popBackStack()
                    navHostController.navigate(value.route)
                }
                is UiEvent.ShowSetBaseUrlScreenErrorMessage->{
                    errorMessage = value.errorMessage
                    showError = true
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SetBaseUrlHeadingSection()
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(

                value = baseUrlText,
                onValueChange = { typedText ->
                    baseUrlText = typedText
                    showError = false
                    errorMessage = null
                },
                placeholder = {
                    Text(
                        text = "Enter Base Url",
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
                        if (urlValidator(baseUrl = baseUrlText)) {
                            setBaseUrlScreenViewModel.setBaseUrl(baseUrl = baseUrlText)
                        } else {
                            setBaseUrlScreenViewModel.setErrorUrlValidationError()
                        }
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
                    if (urlValidator(baseUrl = baseUrlText)) {
                        setBaseUrlScreenViewModel.setBaseUrl(baseUrl = baseUrlText)
                    } else {
                        setBaseUrlScreenViewModel.setErrorUrlValidationError()
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
                    text = "Set Base Url",
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

private fun urlValidator(baseUrl: String): Boolean {
    return try {
        URLUtil.isValidUrl(baseUrl) && Patterns.WEB_URL.matcher(baseUrl).matches()
    } catch (e: Exception) {
        false
    }
}

