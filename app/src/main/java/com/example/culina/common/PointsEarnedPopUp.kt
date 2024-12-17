package com.example.culina.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.culina.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun PointsEarnedPopUp(pointsEarned: Int, reason: String, navController: NavController) {
    Box(
        modifier = Modifier
            .shadow(elevation = 6.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
            .size(150.dp)
            .clickable { navController.navigate("home") },
        contentAlignment = Alignment.Center,

        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "+ $pointsEarned",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                reason,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

        }
    }
}

@Composable
fun PointsEarnedAnimation(pointsEarned: Int, reason: String, navController: NavController) {
    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        visible = true
        delay(3000)
        visible = false
    }
    AnimatedVisibility(visible, enter = scaleIn(), exit = scaleOut()) {
        PointsEarnedPopUp(pointsEarned, reason, navController)
    }
}

@Preview
@Composable
fun PointsEarnedPopUpPreview() {
    AppTheme(dynamicColor = false) {
        Box(Modifier.background(Color.White)) {
            PointsEarnedPopUp(50, "this is a test", rememberNavController())
        }
    }
}