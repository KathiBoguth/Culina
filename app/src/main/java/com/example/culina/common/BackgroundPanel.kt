package com.example.culina.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun BackgroundPanel(innerPadding: PaddingValues, content: @Composable () -> Unit) {
    val color = MaterialTheme.colorScheme.background
    val colorWithOpacity =
        Color(color.red.toFloat(), color.green.toFloat(), color.blue.toFloat(), 0.5f)
    Box(
        modifier = with(Modifier) {
            fillMaxSize()
                .padding(
                    PaddingValues(
                        start = innerPadding.calculateStartPadding(
                            LayoutDirection.Ltr
                        ) + 24.dp,
                        end = innerPadding.calculateEndPadding(
                            LayoutDirection.Ltr
                        ) + 24.dp,
                        top = innerPadding.calculateTopPadding() + 24.dp,
                        bottom = innerPadding.calculateBottomPadding() + 108.dp
                    )
                )
                .clip(RoundedCornerShape(12.dp))
                .background(colorWithOpacity)

        }) { content() }
}
