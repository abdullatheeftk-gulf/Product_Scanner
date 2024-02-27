package com.gulftechiinovations.product_scanner.screens.home_screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gulftechiinovations.product_scanner.R
import com.gulftechiinovations.product_scanner.screens.home_screen.components.HeadingSection
import com.gulftechiinovations.product_scanner.screens.home_screen.components.ImageSection
import com.gulftechiinovations.product_scanner.screens.home_screen.components.TextSection
import com.gulftechiinovations.product_scanner.screens.ui_util.UiEvent
import com.gulftechiinovations.product_scanner.screens.ui_util.screenSizeCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    hideKeyboard: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {

    var showProgressBar by remember {
        mutableStateOf(false)
    }

    //val screenSize = screenSizeCalculator()

    val product by homeScreenViewModel.productState

    val barcode by homeScreenViewModel.barcode

    val scannedBarcode by homeScreenViewModel.scannedBarcode

    //val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val baseUrl by homeScreenViewModel.baseUrl

    val showIdleLogo by homeScreenViewModel.showIdleLogo

    val companyName by homeScreenViewModel.companyName

    val logoByteArray by homeScreenViewModel.companyLogo
    
    LaunchedEffect(key1 = showIdleLogo){
        Log.e(TAG, "HomeScreen: $showIdleLogo", )
        hideKeyboard()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        //homeScreenViewModel.setDeviceSize(screenSize)

        homeScreenViewModel.uiEvent.collectLatest { value ->
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
                    /*delay(3000L)
                    navHostController.popBackStack()
                    navHostController.navigate(value.route)*/
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
        //hideKeyboard()
        it.calculateTopPadding()
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadingSection(companyName = companyName)
            product?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageSection(baseUrl = baseUrl, scannedBarcode = scannedBarcode)
                    TextSection(product = it, scannedBarcode = scannedBarcode)
                }
            }
            if (showIdleLogo) {
                if(logoByteArray!=null){
                    val bitmap = convertImageByteArrayToBitmap(imageData = logoByteArray!!)
                    Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.size(500.dp))
                }else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Scan Here", fontSize = 80.sp, color = Color.Red)
                            Icon(
                                painter = painterResource(id = R.drawable.down_arrow),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(300.dp),
                                tint = Color.Red
                            )
                        }

                    }
                }
            }
            if (showProgressBar) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            OutlinedTextField(
                value = barcode,
                onValueChange = { value ->
                    homeScreenViewModel.setBarcode(value)

                    if (value.endsWith("\n")) {
                        // Toast.makeText(context, barcodeScanned, Toast.LENGTH_LONG).show()
                        homeScreenViewModel.getProduct(barcode = barcode)
                        homeScreenViewModel.setBarcode("")
                        hideKeyboard()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(focusRequester),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = MaterialTheme.colorScheme.background,
                    unfocusedTextColor = MaterialTheme.colorScheme.background,
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 1.sp)

            )

        }


    }

}

private fun convertImageByteArrayToBitmap(imageData: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
}

/*
@Preview
@Composable
fun HomeScreenPrev() {
    HomeScreen()
}*/
