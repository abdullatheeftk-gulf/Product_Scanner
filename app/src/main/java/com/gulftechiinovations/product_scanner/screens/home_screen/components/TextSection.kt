package com.gulftechiinovations.product_scanner.screens.home_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gulftechiinovations.product_scanner.R
import com.gulftechiinovations.product_scanner.models.Product

@Composable
fun RowScope.TextSection(
    product: Product,
    width: Dp,
) {
    val notoArabicFonts = FontFamily(
        Font(R.font.arabic_regular,FontWeight.W400),
        Font(R.font.arabic_medium,FontWeight.W500),
        Font(R.font.arabic_semi_bold,FontWeight.W600),
        Font(R.font.arabic_bold,FontWeight.W700)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
       // Text(text = scannedBarcode)
        Text(
            text = product.productName,
           // fontSize = 50.sp,
            fontSize =if(width>=900.dp) 50.sp else 22.sp,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            //lineHeight = 50.sp,
            lineHeight = if(width>=900.dp) 50.sp else 22.sp
        )
        Spacer(modifier = Modifier.height(
            //16.dp
            if(width>=900.dp) 16.dp else 8.dp
        ))
        Text(
            text = product.productLocalName ?: "",
           // fontSize = 48.sp,
            fontSize =if(width>=900.dp) 48.sp else 20.sp,
            color = Color(0xFF016B06),
            fontWeight = FontWeight.W600,
            //lineHeight = 48.sp,
            lineHeight = if(width>=900.dp) 48.sp else 20.sp,
            textAlign = TextAlign.Center,
            fontFamily = notoArabicFonts
        )
        Spacer(modifier = Modifier.height(
            //36.dp
            if(width>=900.dp) 36.dp else 16.dp
        ))
        Text(
            text = "${product.productPrice} SAR",
            //fontSize = 112.sp,
            fontSize =if(width>=900.dp) 120.sp else 60.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
           // lineHeight = 120.sp,
            lineHeight = if(width>=900.dp) 120.sp else 60.sp
        )
    }
}