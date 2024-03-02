package com.gulftechiinovations.product_scanner.screens.splash_screen.componenets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LicenseDialog(
    message: String,
    width:Dp,
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(
                onClick = { onConfirmButtonClicked() },
                modifier = Modifier.padding(
                    //top = 24.dp,
                    top = if(width>=900.dp) 24.dp else 8.dp,
                   // bottom = 16.dp,
                    bottom =if(width>=900.dp) 16.dp else 6.dp,
                    //end = 16.dp,
                    end = if(width>=900.dp) 16.dp else 6.dp,
                   // start = 16.dp
                    start = if(width>=900.dp) 16.dp else 6.dp,
                )

            ) {
                Text(
                    text = "Ok",
                    //fontSize = 36.sp,
                    fontSize = if(width>=900.dp) 36.sp else 14.sp,
                    modifier = Modifier.padding(
                        //8.dp
                        if(width>=900.dp) 8.dp else 4.dp
                    )
                )
            }
        },
        title = {
            Text(
                text = message,
               // fontSize = 36.sp,
                fontSize = if(width>=900.dp) 36.sp else 18.sp,
            )
        }
    )

}