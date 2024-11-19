package com.example.culina

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.culina.ui.theme.AppTheme

@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    NavigationBar(
        modifier = Modifier
            .height(108.dp),
        containerColor = MaterialTheme.colorScheme.onSecondary,
        contentColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        val list = listOf<BottomNavItem>(
            BottomNavItem.Home, BottomNavItem.Quiz,
            BottomNavItem.Cooking, BottomNavItem.Social
        )
        list.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = Modifier.padding(16.dp),
                icon = {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(item.iconId),
                        contentDescription = item.label,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                    )
                },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index; navController.navigate(item.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
                },
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    AppTheme(dynamicColor = false) {
        BottomNavigationBar(rememberNavController())

    }
}