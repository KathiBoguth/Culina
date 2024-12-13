package com.example.culina.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.culina.common.BackgroundPanel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@Composable

fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()

    val score by viewModel.score.collectAsState(0)

    BackgroundPanel(innerPadding) {

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Culina",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleLarge
            )

            Text("Score:")
            Text(score.toString())

            Button(onClick = {
                val resultFlow = viewModel.signOut()
                coroutineScope.launch {
                    resultFlow.take(2).collect {
                        if (it) {
                            navController.navigate("signIn")
                        }
                    }
                }
            }) {
                Text("Sign out")
            }

        }
    }
}