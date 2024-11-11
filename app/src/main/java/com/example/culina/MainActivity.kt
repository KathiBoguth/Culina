package com.example.culina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.culina.dailyCooking.DailyCookingOverview
import com.example.culina.home.Home
import com.example.culina.quiz.Quiz
import com.example.culina.social.Social
import com.example.culina.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            AppTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = with(Modifier) {
                            fillMaxSize()
                                .paint(
                                    painterResource(id = R.drawable.kitchen),
                                    contentScale = ContentScale.Crop
                                )

                        }) {
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
                                    .alpha(0.5f)
                                    .background(MaterialTheme.colorScheme.background)

                            }) {
                            AppNavHost(navController = navController)
                        }
                        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
                            BottomNavigationBar(navController)

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = BottomNavItem.Home.route,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(BottomNavItem.Home.route) { Home() }
        composable(BottomNavItem.Quiz.route) { Quiz() }
        composable(BottomNavItem.Cooking.route) { DailyCookingOverview() }
        composable(BottomNavItem.Social.route) { Social() }
    }
}