package com.gulftechiinovations.product_scanner.screens.set_base_url_screen.componenets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetBaseUrlHeadingSection(
    width:Dp
) {
    Surface(shadowElevation = 6.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    //150.dp
                    if (width>=900.dp) 150.dp else 70.dp
                ),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                color = Color(0xFF02842A)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier,
                        text = "SET BASE URL",
                        textAlign = TextAlign.Center,
                        //fontSize = 50.sp,
                        fontSize = if(width>=900.dp) 50.sp else 22.sp,
                        color = Color.White,
                        fontWeight = FontWeight(400),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

}