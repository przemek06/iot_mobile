package edu.pwr.iotmobile.androidimcs.ui.screens.learn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

private const val LINE_HEIGHT = 20

@Composable
fun LearnScreen(navigation: LearnScreenNavigation) {
    LearnScreenContent(navigation)
}

@Composable
fun LearnScreenContent(navigation: LearnScreenNavigation) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            onReturn = { navigation.onReturn() }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimensions.space22)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.s92),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 35.sp
                )
                Dimensions.space30.HeightSpacer()
            }

            // App Layout
            item {
                Text(
                    text = stringResource(id = R.string.s93),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space30.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s94),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space12.HeightSpacer()
            }

            // Project
            item {
                Text(
                    text = stringResource(id = R.string.s95),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s96),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space22.HeightSpacer()
            }

            // Dashboard
            item {
                Text(
                    text = stringResource(id = R.string.s97),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s98),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space22.HeightSpacer()
            }

            // Topic
            item {
                Text(
                    text = stringResource(id = R.string.s99),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s100),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space22.HeightSpacer()
            }

            // Component
            item {
                Text(
                    text = stringResource(id = R.string.s101),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s102),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space22.HeightSpacer()
            }

            // First steps
            item {
                Text(
                    text = stringResource(id = R.string.s103),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space12.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s104),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space30.HeightSpacer()
            }

            // Bullet points
            item {
                Text(
                    text = stringResource(id = R.string.s105),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space4.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.s106),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space4.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.s107),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s108),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            // Bullet point
            item {
                Text(
                    text = stringResource(id = R.string.s109),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s110),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space8.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s111),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space30.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.s112),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = LINE_HEIGHT.sp
                )
                Dimensions.space30.HeightSpacer()
            }

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    ButtonCommon(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.a_s63),
                        onClick = { navigation.openProjects() }
                    )
                }
                Dimensions.space60.HeightSpacer()
            }
        }
    }
}