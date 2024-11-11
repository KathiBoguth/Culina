package com.example.culina.common

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CulinaButton() {
    Button(onClick = {
        println("clicked!")
    }, colors = colorS) {
        Text("Cooked Today?")
 }
}