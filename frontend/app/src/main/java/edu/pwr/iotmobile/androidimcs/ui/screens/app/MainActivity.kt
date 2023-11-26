package edu.pwr.iotmobile.androidimcs.ui.screens.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.BottomNavigationBar
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.theme.AndroidIMCSTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    // TODO: delete if registerLauncher works
    private var intentState: MutableState<Intent?> = mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: get client + repo -> listen to invitations + send value to AppContent (red dot)
        setContent {
            AndroidIMCSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent(intentState.value)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("Trigger", "onNewIntent called")
        Log.d("Trigger", intent.toString())
        super.onNewIntent(intent)
    }

    // TODO: on cośtam dispose of rSOcket
}

@Composable
private fun AppContent(intent: Intent? = null) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    viewModel.toast.CollectToast(context)

    val startDestination =
        if (uiState.isUserLoggedIn) Screen.Main.path
        else Screen.Login.path

    BottomNavigationBar(
        navController = navController,
        startDestination = startDestination
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidIMCSTheme {
        AppContent()
    }
}
