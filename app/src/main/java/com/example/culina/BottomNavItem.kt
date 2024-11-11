package com.example.culina

sealed class BottomNavItem(val route: String, val iconId: Int, val label: String) {
    object Home : BottomNavItem("home", R.drawable.baseline_home_24, "Home")
    object Quiz :
        BottomNavItem("quiz", R.drawable.baseline_quiz_24, "Quiz")

    object Cooking : BottomNavItem("cooking", R.drawable.skillet_24px, "Cooking")
    object Social : BottomNavItem("social", R.drawable.baseline_people_24, "Social")


}


