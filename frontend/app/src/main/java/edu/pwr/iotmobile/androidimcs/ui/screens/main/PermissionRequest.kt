package edu.pwr.iotmobile.androidimcs.ui.screens.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionRequest() {
    val permission = Manifest.permission.POST_NOTIFICATIONS
    val showDialog = remember { mutableStateOf(false) }

    val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted && !showDialog.value) {
                showDialog.value = true
            } else if (isGranted) {
                showDialog.value = false
            }
        }
    )

    LaunchedEffect(Unit) {
        notificationPermissionResultLauncher.launch(permission)
    }

    if (showDialog.value) {
        val activity = LocalContext.current as ComponentActivity
        PermissionDialog(
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                LocalContext.current as ComponentActivity,
                permission
            ),
            onDismiss = {
                showDialog.value = false
            },
            onOkClick = {
                notificationPermissionResultLauncher.launch(permission)
            },
            onGoToAppSettingsClick = activity::openAppSettings
        )
    }
}

@Composable
private fun PermissionDialog(
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ButtonCommon(
                text = if(isPermanentlyDeclined) { "Go to app settings" }
                    else { "Grant permission" }
            ) {
                if (isPermanentlyDeclined)
                    onGoToAppSettingsClick()
                else
                    onOkClick()
            }
        },
        dismissButton = {
            ButtonCommon(
                text = "Decline"
            ) { onDismiss() }
        },

        title = {
            Text(text = "Permission required")
        },
        text = {
            Text(
                if (isPermanentlyDeclined) {
                    "It seems you permanently declined notifications permission. " +
                            "You can go to the app settings to grant it."
                } else {
                    "This app uses notifications as part of its functionality."
                }
            )
        }
    )
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}