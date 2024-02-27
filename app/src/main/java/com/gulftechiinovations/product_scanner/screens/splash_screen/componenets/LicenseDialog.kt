package com.gulftechiinovations.product_scanner.screens.splash_screen.componenets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LicenseDialog(
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(
                onClick = { onConfirmButtonClicked() },
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp, end = 16.dp, start = 16.dp)
            ) {
                Text(text = "Ok", fontSize = 36.sp,modifier = Modifier.padding(8.dp))
            }
        },
        title = {
            Text(text = message, fontSize = 36.sp,)
        }
    )

}