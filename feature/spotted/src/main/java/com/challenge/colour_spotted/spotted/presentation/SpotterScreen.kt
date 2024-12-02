package com.challenge.colour_spotted.spotted.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.challenge.colour_spotter.ui.component.TopBar
import com.challenge.colour_spotter.ui.theme.ColourSpotterTheme

@Composable
fun SpotterScreen (
    navController: NavHostController,
    viewModel: SpotterViewModel = hiltViewModel()
) {
    ListContent()
}

@Composable
private fun ListContent(

    )
{
    Scaffold(
        topBar = {
            TopBar(
                title = "NAME APP",//stringResource(id = R.string.app_name),
                onBackClick = null,
                actions = {
                    IconButton(onClick = {
                        //TODO
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "none"
                        )
                    }
                }
            )
        }) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ListScreenPreview() {
    ColourSpotterTheme {
        ListContent()
    }
}
