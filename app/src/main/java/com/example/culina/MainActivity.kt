package com.example.culina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.culina.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val imagePositions: List<Float> = listOf(
                -1f, -0.3f, 0.3f, 1f
            )
            var positionIndex by remember { mutableIntStateOf(0) }
            val currentPosition by animateFloatAsState(
                imagePositions[positionIndex],
                label = "BackgroundMove"
            )
            navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
                positionIndex =
                    listOf(
                        "home",
                        "quiz",
                        "cooking",
                        "social"
                    ).indexOf(destination.route)
            })

            AppTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = with(Modifier) {
                            fillMaxSize()
                                .paint(
                                    painterResource(id = R.drawable.kitchen),
                                    alignment = BiasAlignment(currentPosition.toFloat(), -1f),
                                    contentScale = ContentScale.Crop
                                )

                        }) {
                        AppNavHost(navController = navController, innerPadding = innerPadding)
                    }
                    Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
                        BottomNavigationBar(navController)

                    }
                }
            }
        }
    }
}
