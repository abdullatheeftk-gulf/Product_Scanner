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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gulftechiinovations.product_scanner.models.Product

@Composable
fun RowScope.TextSection(
    product: Product,
    scannedBarcode:String
) {
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
            fontSize = 50.sp,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = product.productLocalName ?: "",
            fontSize = 48.sp,
            color = Color(0xFF016B06),
            fontWeight = FontWeight(300),
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(text = "${product.productPrice} SAR", fontSize = 96.sp, fontWeight = FontWeight.Bold, color = Color.Red)
    }
}