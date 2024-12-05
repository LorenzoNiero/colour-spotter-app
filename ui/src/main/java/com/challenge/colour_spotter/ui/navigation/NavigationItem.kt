package com.challenge.colour_spotter.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Class that manages the route name of destination and parameters to pass
 */
sealed class NavigationItem(val baseRoute: String) {
    open val route: String = baseRoute

    object CAMERA : NavigationItem(ScreenEnum.CAMERA.name)
    object LIST : NavigationItem(ScreenEnum.LIST.name)
}

enum class ScreenEnum() {
    CAMERA,
    LIST,
}