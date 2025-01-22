package com.interview.myapplication.ui.navigation

sealed class AppScreen(val route: String) {

    data object DashBoard : AppScreen("dashboard")
    data object Details : AppScreen("details")
}
