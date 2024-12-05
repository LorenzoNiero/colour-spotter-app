package com.challenge.colour_spotted.list.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.challenge.colour_spotted.list.presentation.model.ListUiState
import com.challenge.colour_spotter.ui.component.TopBar
import com.challenge.colour_spotter.ui.theme.ColourSpotterTheme
import com.challenge.colour_spotter.ui.R as R_UI

@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: ListViewModel = hiltViewModel()
) {

    ListSpotterContent(
        navController = navController
    )

}

@Composable
private fun ListSpotterContent(
    uistate : ListUiState,
    navController: NavHostController? = null
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R_UI.string.app_name),
                onBackClick = {
                    navController?.popBackStack()
                }
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                  when (uiState) {
                    ListUiState.Empty -> {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 16.dp)
                            ,
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.empty_list_message)
                        )
                    }
                    is ListUiState.Error -> {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = dimensionResource(R_UI.dimen.padding_list_vertical)),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.error_label, uiState.message ?: "")
                        )
                    }
                    ListUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is ListUiState.Result -> {

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                vertical =  dimensionResource(R_UI.dimen.padding_list_vertical),
                                horizontal =  dimensionResource(R_UI.dimen.padding_list_horizontal)
                            )
                        ) {

                        }
                    }
                }
            }

        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Loading() {
    ColourSpotterTheme {
        ListSpotterContent(

        )
    }
}
