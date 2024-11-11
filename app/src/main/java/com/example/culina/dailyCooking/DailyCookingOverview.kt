package com.example.culina.dailyCooking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.culina.common.CulinaButtonPrimary

@Composable
fun DailyCookingOverview() {
    Row(
        Modifier
            .padding(20.dp)
            .fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CulinaButtonPrimary()
        }
    }
}