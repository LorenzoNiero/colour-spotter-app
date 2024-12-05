package com.challenge.colourspotter.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.challenge.colour_spotted.list.presentation.ListScreen
import com.challenge.colour_spotted.spotted.presentation.SpotterScreen
import com.challenge.colour_spotter.ui.navigation.NavigationItem

/**
 * Component that manages navigation through pages at the change of route
 * @param navController navigation manager
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.CAMERA.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.CAMERA.route) {
            SpotterScreen(navController)
        }
        composable(NavigationItem.LIST.route) {
            ListScreen(navController)
        }

    }
}

