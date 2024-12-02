package com.challenge.colour_spotter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.challenge.colour_spotter.ui.theme.ColourSpotterTheme
import com.challenge.colourspotter.navigation.AppNavHost


@Composable
fun ColourSpotterApp() {
    ColourSpotterTheme {
        val navController = rememberNavController()
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavHost(navController)
        }
    }

}