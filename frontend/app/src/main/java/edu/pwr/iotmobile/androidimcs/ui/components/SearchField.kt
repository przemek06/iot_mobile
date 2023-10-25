package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.pwr.iotmobile.androidimcs.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    text: String,
    modifier: Modifier = Modifier,
    onValueChange: (v: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        leadingIcon = { Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Search",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        ) },
        label = { Text(text = stringResource(id = R.string.search)) },
        trailingIcon = {
            IconButton(onClick = { onValueChange("") }) {
                Image (
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        ) } },
//        textStyle = LocalTextStyle.current.copy(
//            fontSize = 10.sp, // Adjust font size as needed
//            fontWeight = FontWeight.Normal
//        )
    )
}