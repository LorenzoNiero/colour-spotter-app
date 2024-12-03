package com.challenge.colour_spotter.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.ui.R
import com.challenge.colour_spotter.ui.theme.ColourSpotterTheme
import android.graphics.Color as AndroidColor

@Composable
fun ColorCell(color: ColorModel, modifier: Modifier = Modifier) {

    var colorHex by remember { mutableStateOf(Color.Unspecified) }

    LaunchedEffect(color.hex) {
        colorHex = Color(AndroidColor.parseColor(color.hex))
    }

    CardSurface(modifier) {
        Row(
            modifier = Modifier
                .defaultMinSize(minWidth = 150.dp, minHeight = 50.dp)
                .padding(dimensionResource(R.dimen.small)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.normal)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(60.dp)
                    .background(
                        color = colorHex,
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_surface_rounded_corner))
                    )
            ) { }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = color.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = color.hex, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Light, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_ColorCell() {
    ColourSpotterTheme {
        Box(modifier = Modifier.width(500.dp)) {
            ColorCell(
                color = ColorModel(
                    name = "Sun Yellow Light",
                    hex = "#fcba03"
                ),
                Modifier.fillMaxWidth()
            )
        }
    }
}