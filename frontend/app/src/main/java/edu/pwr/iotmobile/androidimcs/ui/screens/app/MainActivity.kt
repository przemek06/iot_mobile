package edu.pwr.iotmobile.androidimcs.ui.screens.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.BottomNavigationBar
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.theme.AndroidIMCSTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: get client + repo -> listen to invitations + send value to AppContent (red dot)
        setContent {
            AndroidIMCSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }

    // TODO: on cośtam dispose of rSOcket
}

@Composable
private fun AppContent() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

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
