package com.gulftechiinovations.product_scanner.screens.ui_util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun screenSizeCalculator(): Pair<Dp,Dp> {
    val configuration = LocalConfiguration.current
    return Pair(configuration.screenWidthDp.dp,configuration.screenHeightDp.dp)
}