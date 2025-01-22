package com.interview.myapplication.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.interview.myapplication.ui.viewmodel.MainActivityViewModel
import com.interview.myapplication.ui.views.dasboard.DashBoardScreen
import com.interview.myapplication.ui.views.details.DetailedScreen

/**
 * AppNavHost composable manages the navigation between different screens in the app,
 * starting with the splash screen and supporting transitions to home and detail screens.
 */
@Composable
fun AppNavHost(navController: NavHostController, recentViewModel: MainActivityViewModel) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.DashBoard.route
    ) {
        composable(
            route = AppScreen.DashBoard.route,
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800)
                )
            }
        ) {
            DashBoardScreen(navController, recentViewModel)
        }

        composable(
            route = AppScreen.Details.route,
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800)
                )
            }
        ) {
            DetailedScreen(recentViewModel)
        }
    }
}