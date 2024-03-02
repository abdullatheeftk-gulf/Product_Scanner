package com.gulftechiinovations.product_scanner.screens.license_activation_screen.componenets


import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gulftechiinovations.product_scanner.models.license.UniLicenseDetails
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseDetailsAlertDialog(
    width:Dp,
    alertDialogMessage:String,
    onDismissRequest:()->Unit,
    onConfirmButtonClicked:()->Unit
) {
    val uniLicenseDetails :UniLicenseDetails = Json.decodeFromString<UniLicenseDetails>(alertDialogMessage)

    val licenceType = uniLicenseDetails.licenseType
    val expirationDate = uniLicenseDetails.expiryDate

    AlertDialog(
        onDismissRequest = { onDismissRequest() },

    ) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(
                    //16.dp
                    if(width>=900.dp) 16.dp else 8.dp
                ),
            ) {
                Text(
                    text = "License Details",
                    //fontSize = 48.sp,
                    fontSize = if(width>=900.dp) 48.sp else 20.sp,
                    color = Color(0xFF2EA101),
                    textDecoration = TextDecoration.Underline,
                )
                Spacer(modifier = Modifier.height(
                    //16.dp
                    if(width>=900.dp) 16.dp else 8.dp
                ))
                Row {
                    Text(
                        text = "License Type",
                       // fontSize = 32.sp,
                        fontSize = if(width>=900.dp) 32.sp else 14.sp,
                        modifier = Modifier.weight(1.8f)
                    )
                    Text(
                        text = ":",
                        //fontSize = 32.sp,
                        fontSize = if(width>=900.dp) 32.sp else 14.sp,
                        modifier = Modifier.weight(0.2f)
                    )
                    Text(
                        text = licenceType,
                        //fontSize = 38.sp,
                        fontSize = if(width>=900.dp) 38.sp else 16.sp,
                        modifier = Modifier.weight(1.4f),
                        color = if (licenceType == "demo") Color.Red else Color(
                            0xFF1B7D00
                        )
                    )
                }
                Row {
                    Text(
                        text = "Expiration date",
                        //fontSize = 32.sp,
                        fontSize = if(width>=900.dp) 32.sp else 14.sp,
                        modifier = Modifier.weight(1.8f)
                    )
                    Text(
                        text = ":",
                        //fontSize = 32.sp,
                        fontSize = if(width>=900.dp) 32.sp else 14.sp,
                        modifier = Modifier.weight(0.2f)
                    )
                    Text(
                        text = expirationDate ?: "Nil",
                        //fontSize = 34.sp,
                        fontSize = if(width>=900.dp) 34.sp else 15.sp,
                        modifier = Modifier.weight(1.4f),
                        color = Color.LightGray,
                    )
                }
                Spacer(modifier = Modifier.height(
                    //16.dp
                    if(width>=900.dp) 16.dp else 8.dp
                ))
                Button(onClick = { onConfirmButtonClicked() }) {
                    Text(
                        text = "Ok",
                        //fontSize = 32.sp
                        fontSize = if(width>=900.dp) 32.sp else 14.sp,
                    )
                }
                Spacer(modifier = Modifier.height(
                    //8.dp
                    if(width>=900.dp) 8.dp else 4.dp
                ))

            }
        }


    }
}