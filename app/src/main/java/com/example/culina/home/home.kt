package com.example.culina.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Home() {
    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Culina",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.titleLarge
        )

    }
}