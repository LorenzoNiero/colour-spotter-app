package com.challenge.colour_spotted.list.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.challenge.colour_spotted.list.R
import com.challenge.colour_spotted.list.presentation.model.ListUiState
import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.ui.component.ColorCell
import com.challenge.colour_spotter.ui.component.TopBar
import com.challenge.colour_spotter.ui.theme.ColourSpotterTheme
import com.challenge.colour_spotter.ui.R as R_UI

/**
 * List colors scannned screen.
 */
@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: ListViewModel = hiltViewModel()
) {

    val uiState = viewModel.listUiState.collectAsState()
    val isDesc = viewModel.isDescUiState.collectAsState()

    ListSpotterContent(
        uiState = uiState.value,
        isDesc = isDesc.value,
        navController = navController
    )

}

@Composable
private fun ListSpotterContent(
    uiState: ListUiState,
    navController: NavHostController? = null,
    isDesc: Boolean,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (uiState) {
                ListUiState.Empty -> {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = dimensionResource(R_UI.dimen.padding_list_vertical)),
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
                            vertical = dimensionResource(R_UI.dimen.padding_list_vertical),
                            horizontal = dimensionResource(R_UI.dimen.padding_list_horizontal)
                        ),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R_UI.dimen.spacing_between_items)),
                        horizontalAlignment = Alignment.End
                    ) {
                        item {
                            IconButton(
                                modifier = Modifier.width(50.dp),
                                onClick = {
                                    uiState.onSort()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowUpward.takeIf { isDesc } ?:  Icons.Filled.ArrowDownward,
                                    contentDescription = "sort"
                                )
                            }
                        }
                        items(items = uiState.colors) { color ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R_UI.dimen.small)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                ColorCell(
                                    color,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    modifier = Modifier.width(50.dp),
                                    onClick = {
                                        uiState.onDelete(color)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = stringResource(
                                            R.string.delete_button_description,
                                            color.name
                                        )
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_List() {
    ColourSpotterTheme {
        ListSpotterContent(uiState = ListUiState.Result(
            colors = listOf(
                ColorModel(
                    id = "FFBB00",
                    name = "Red",
                    hex = "#FF0000"
                ),
                ColorModel(
                    id = "FFBB00",
                    name = "Blue",
                    hex = "#FFBB00"
                )
            ),
            {},
            {}
        ), isDesc = true)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Error() {
    ColourSpotterTheme {
        ListSpotterContent(uiState = ListUiState.Error("message error"), isDesc = true)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Empty() {
    ColourSpotterTheme {
        ListSpotterContent(uiState = ListUiState.Empty, isDesc = true)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Loading() {
    ColourSpotterTheme {
        ListSpotterContent(uiState = ListUiState.Loading, isDesc = true)
    }
}
