package com.example.culina.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.culina.common.BackgroundPanel
import com.example.culina.ui.theme.AppTheme
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@Composable

fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()

    val score by animateIntAsState(viewModel.score.collectAsState(0).value, label = "score")
    val name by viewModel.name.collectAsState("")

    BackgroundPanel(innerPadding) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Culina",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleLarge
            )

            Welcome(name)

            Score(score)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Score(score: Int) {

    val stopIndicatorColor = MaterialTheme.colorScheme.primary

    val level = (score / 100) + 1
    val progress: Float by animateFloatAsState(score.toFloat() % 100, label = "progress")
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Your Current Level:"
        )
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayLarge,
            text = level.toString()
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { progress / 100 },
                modifier = Modifier
                    .height(8.dp)
                    .scale(scaleX = 0.9F, scaleY = 1F),
                strokeCap = StrokeCap.Round,
                drawStopIndicator = {
                    drawStopIndicator(
                        drawScope = this,
                        stopSize = 8.dp,
                        color = stopIndicatorColor,
                        strokeCap = StrokeCap.Round
                    )
                }
            )
            Text("Lvl ${level + 1}", style = MaterialTheme.typography.labelSmall)

        }
    }
}

@Composable
fun Welcome(name: String) {
    Text("Welcome $name!", style = MaterialTheme.typography.displayMedium)
}

@Composable
@Preview
fun ScorePreview() {
    AppTheme(dynamicColor = false) {
        BackgroundPanel(PaddingValues(20.dp)) {
            Score(50)
        }
    }
}
