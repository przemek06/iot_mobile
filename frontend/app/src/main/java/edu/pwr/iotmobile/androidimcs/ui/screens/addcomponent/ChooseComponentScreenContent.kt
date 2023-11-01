package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.gray

@Composable
fun ChooseComponentScreenContent(
    uiState: AddComponentUiState
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.space30),
        verticalArrangement = Arrangement.spacedBy(Dimensions.space14)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                text = stringResource(id = R.string.s27),
                style = MaterialTheme.typography.titleSmall
            )
            Dimensions.space10.HeightSpacer()
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = stringResource(id = R.string.s30),
                style = MaterialTheme.typography.bodyLarge
            )
            Dimensions.space10.HeightSpacer()
        }

        items(uiState.inputComponents) {
            ComponentItem()
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = stringResource(id = R.string.s31),
                style = MaterialTheme.typography.bodyLarge
            )
            Dimensions.space10.HeightSpacer()
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = stringResource(id = R.string.s32),
                style = MaterialTheme.typography.bodyLarge
            )
            Dimensions.space10.HeightSpacer()
        }
    }
}

@Composable
private fun ComponentItem(
    isSelected: Boolean = false
) {
    val borderWidth =
        if (isSelected) 3.dp
        else 2.dp
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.gray  // TODO: change based on
        else MaterialTheme.colorScheme.background
    Card(
        modifier = Modifier
            .height(120.dp)
            .clickable { /*TODO*/ },
        border = BorderStroke(width = borderWidth, color = borderColor),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.space10),
        ) {
            Text(
                text = "Slider",
                style = MaterialTheme.typography.bodySmall
            )
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_slider),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                    contentDescription = null,
                )
            }
        }
    }
}