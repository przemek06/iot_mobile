package edu.pwr.iotmobile.androidimcs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.BottomNavigationBar
import edu.pwr.iotmobile.androidimcs.ui.theme.AndroidIMCSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

@Composable
private fun AppContent() {
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidIMCSTheme {
        AppContent()
    }
}
