package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.gray

private val BOTTOM_BAR_HEIGHT = 80.dp

@Composable
fun AddComponentScreen() {
    AddComponentScreenContent()
}

@Composable
private fun AddComponentScreenContent() {
    NavigationWrapper {
        Text(text = "hello")
    }
}

@Composable
private fun NavigationWrapper(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.TopStart)
        ) {
            TopBar(
                text = stringResource(id = R.string.add_component),
                onReturn = {/*TODO*/}
            )
            Dimensions.space14.HeightSpacer()
            Column(modifier = Modifier.padding(horizontal = Dimensions.space22)) {
                content()
            }
        }
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun BottomNavigationBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BOTTOM_BAR_HEIGHT)
            .background(color = MaterialTheme.colorScheme.gray)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space14)
        ) {
            ButtonCommon(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = "Next",
                onClick = {/*TODO*/ }
            )
        }
    }
}