package com.gulftechiinovations.product_scanner.screens.home_screen.components


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gulftechiinovations.product_scanner.R
import com.gulftechiinovations.product_scanner.util.HttpConstants

//private const val TAG = "ImageSection"
@Composable
fun RowScope.ImageSection(
    scannedBarcode:String,
    baseUrl:String
) {
    val imageUrl = "$baseUrl${HttpConstants.GET_PRODUCT_IMAGE_BY_BARCODE}$scannedBarcode"
    //Log.w(TAG, "ImageSection: $imageUrl", )

    var showProgressBar by remember {
        mutableStateOf(false)
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = null,
                onLoading = {
                    //Log.i(TAG, "ImageSection: $it")
                    showProgressBar = true
                },
                onSuccess = {
                   // Log.d(TAG, "ImageSection: $it")
                    showProgressBar = false
                },
                modifier = Modifier
                    .size(600.dp)
                    .aspectRatio(1f)
                    .fillMaxWidth(),
                //.alpha(alpha = if (showProgressBarInItem && selectedIndex == index) ContentAlpha.disabled else ContentAlpha.high),
                error = painterResource(id = R.drawable.no_image),
                onError = {
                    showProgressBar = false
                   // Log.e(TAG, "ImageSection: ${it.result.throwable.message}")
                }
            )
            if(showProgressBar){
                CircularProgressIndicator()
            }
        }

    }


}