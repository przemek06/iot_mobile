package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckboxWithTitle(
    isChecked: Boolean,
    title: String,
    onCheckedChange: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange() }
        )

        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = title
        )
    }
}