package com.example.culina

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.culina.authentication.signin.SignInScreen
import com.example.culina.authentication.signup.SignUpScreen
import com.example.culina.dailyCooking.DailyCookingOverview
import com.example.culina.home.HomeScreen
import com.example.culina.quiz.Quiz
import com.example.culina.social.Social
import kotlin.collections.indexOf


val navItemOrder = listOf("home", "quiz", "cooking", "social")

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: String = "signin",//BottomNavItem.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("signin")
        {
            SignInScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }
        composable("signup")
        {
            SignUpScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }
        composable(
            BottomNavItem.Home.route,
            enterTransition = {
                return@composable slideIn(initialState, targetState)
            },
            exitTransition = { return@composable slideOut(initialState, targetState) }) {
            HomeScreen(
                innerPadding = innerPadding,
                navController = navController
            )
        }
        composable(BottomNavItem.Quiz.route, enterTransition = {
            return@composable slideIn(initialState, targetState)
        }, exitTransition = { return@composable slideOut(initialState, targetState) }) {
            Quiz(
                innerPadding
            )
        }
        composable(
            BottomNavItem.Cooking.route,
            enterTransition = {
                return@composable slideIn(initialState, targetState)
            },
            exitTransition = { return@composable slideOut(initialState, targetState) }) {
            DailyCookingOverview(
                innerPadding,
                navController
            )
        }
        composable(BottomNavItem.Social.route, enterTransition = {
            return@composable slideIn(initialState, targetState)
        }, exitTransition = { return@composable slideOut(initialState, targetState) }) {
            Social(
                innerPadding = innerPadding
            )
        }
    }
}

fun slideIn(initialState: NavBackStackEntry, targetState: NavBackStackEntry): EnterTransition {
    return if (navItemOrder.indexOf(initialState.destination.route) > navItemOrder.indexOf(
            targetState.destination.route
        )
    ) {
        slideInHorizontally(initialOffsetX = { -it })
    } else {
        slideInHorizontally(initialOffsetX = { it })
    }

}

fun slideOut(initialState: NavBackStackEntry, targetState: NavBackStackEntry): ExitTransition {
    return if (navItemOrder.indexOf(initialState.destination.route) > navItemOrder.indexOf(
            targetState.destination.route
        )
    ) {
        slideOutHorizontally(targetOffsetX = { it })
    } else {
        slideOutHorizontally(targetOffsetX = { -it })
    }

}