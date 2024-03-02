package com.gulftechiinovations.product_scanner.screens.splash_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gulftechiinovations.product_scanner.R
import com.gulftechiinovations.product_scanner.navigation.RootNavScreens
import com.gulftechiinovations.product_scanner.screens.splash_screen.componenets.LicenseDialog
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import com.gulftechiinovations.product_scanner.screens.ui_util.screenSizeCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "SplashScreen"
@Composable
fun SplashScreen(
    navHostController: NavHostController,
    splashScreenViewModel: SplashScreenViewModel = hiltViewModel<SplashScreenViewModel>()
) {

    val screeSizes = screenSizeCalculator()
    val width = screeSizes.first
    Log.d(TAG, "SplashScreen: $width")

    var showProgressBar by remember {
        mutableStateOf(false)
    }

    var showBaseUrlButton by remember {
        mutableStateOf(false)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    var showAlertDialog by remember{
        mutableStateOf(false)
    }

    var alertDialogMessage by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true){
        splashScreenViewModel.uiEvent.collectLatest {value: UiEvent ->
            when(value){
                is UiEvent.ShowProgressBar->{
                    showProgressBar = true
                }
                is UiEvent.CloseProgressBar->{
                    showProgressBar = false

                }
                is UiEvent.ShowSetBaseUrlButton->{
                    showBaseUrlButton = true
                }
                is UiEvent.ShowSnackBar->{
                    snackBarHostState.showSnackbar(value.message, duration = SnackbarDuration.Long)
                }
                is UiEvent.Navigate->{
                    delay(3000L)
                    navHostController.popBackStack()
                    navHostController.navigate(value.route)
                }
                is UiEvent.ShowAlertDialog->{
                    alertDialogMessage = value.message
                    showAlertDialog = true

                }
                else->Unit
            }
        }
    }

    if(showAlertDialog && alertDialogMessage.isNotEmpty()){
        LicenseDialog(
            message = alertDialogMessage,
            width = width,
            onDismissRequest = {
                showAlertDialog = false
                alertDialogMessage = ""
                // Navigate to License Activation screen
                // ToDo
            }
        ) {
            showAlertDialog = false
            alertDialogMessage = ""
            // Navigate to License Activation screen
            splashScreenViewModel.navigateToLicenseActivationScreen()
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            if (showBaseUrlButton){
                ExtendedFloatingActionButton(
                    containerColor = Color(0xFF029C09),
                    contentColor = Color.White,
                    modifier = Modifier.padding(
                       // vertical = 66.dp
                        vertical = if(width>=900.dp)  66.dp else 30.dp
                    ),
                    onClick = splashScreenViewModel::onSetBaseUrlButtonClicked,
                    shape = CircleShape
                ) {
                    Text(
                        text = "SET BASE URL",
                        //fontSize = 48.sp,
                        fontSize = if(width>=900.dp) 48.sp else 20.sp,
                        modifier = Modifier.padding(
                           // all = 16.dp
                            all = if(width>=900.dp) 16.dp else 8.dp
                        )
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        it.calculateTopPadding()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.size(
                // 500.dp
                if(width>=900.dp) 500.dp else 225.dp
            ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.unipos_logo),
                    contentDescription = null
                )
            }
        }
    }
}



