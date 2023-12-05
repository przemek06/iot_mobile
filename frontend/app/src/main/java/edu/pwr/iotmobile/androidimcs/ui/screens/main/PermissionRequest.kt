package edu.pwr.iotmobile.androidimcs.ui.screens.main

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.webkit.PermissionRequest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.KeyEventDispatcher.Component
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.pwr.iotmobile.androidimcs.service.NotificationService
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionRequest() {

    val permission = Manifest.permission.POST_NOTIFICATIONS
    val isPermanentlyDeclined = remember {
        mutableStateOf(false)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    val activity = LocalContext.current as ComponentActivity

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

    if (showDialog.value)
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

@Composable
fun PermissionDialog(
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ButtonCommon(
                text = if(isPermanentlyDeclined) { "Go" }
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