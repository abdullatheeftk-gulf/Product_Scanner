package com.gulftechiinovations.product_scanner.screens.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gulftechiinovations.product_scanner.R

@Composable
fun HeadingSection(
     companyName:String?
) {
    Surface(shadowElevation = 6.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.unipos_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
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
                        text = companyName ?:"Null",
                        textAlign = TextAlign.Center,
                        fontSize = 50.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

}