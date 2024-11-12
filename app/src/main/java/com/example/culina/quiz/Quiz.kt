package com.example.culina.quiz

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.culina.common.BackgroundPanel

@Composable
fun Quiz(innerPadding: PaddingValues) {
    BackgroundPanel(innerPadding) {

        Text("Quiz")
    }
}