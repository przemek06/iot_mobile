package edu.pwr.iotmobile.androidimcs.ui.screens.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.model.listener.InvitationAlertWebSocketListener
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import edu.pwr.iotmobile.androidimcs.ui.navigation.BottomNavigationBar
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.theme.AndroidIMCSTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private var activity: MutableState<Activity?> = mutableStateOf(null)
    private var isInvitation: MutableState<Boolean> = mutableStateOf(false)
    private var invitationAlertWebSocketListener: InvitationAlertWebSocketListener? = null
    private val userRepository: UserRepository by inject()
    private val client: OkHttpClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.value = this

        CoroutineScope(Dispatchers.Default).launch {
            userRepository.getLoggedInUser().collect {
                if (it != null) {
                    invitationAlertWebSocketListener?.closeWebSocket()
                    invitationAlertWebSocketListener = InvitationAlertWebSocketListener(
                        client = client,
                        onNewInvitation = { data -> onNewInvitation(data) }
                    )
                }
            }
        }

        setContent {
            AndroidIMCSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent(isInvitation.value)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        invitationAlertWebSocketListener?.closeWebSocket()
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("Trigger", "onNewIntent called")
        Log.d("Trigger", intent.toString())
        super.onNewIntent(intent)
    }

    private fun onNewInvitation(data: Boolean) {
        Log.d("Invitation", "onNewInvitation called")
        Log.d("Invitation", "data: $data")
        isInvitation.value = data
        if (data) {
            activity.value?.runOnUiThread {
                Toast.makeText(activity.value, "You have a new invitation!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

@Composable
private fun AppContent(isInvitation: Boolean) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    viewModel.toast.CollectToast(context)

    val startDestination =
        if (uiState.isUserLoggedIn) Screen.Main.path
        else Screen.Login.path

    AnimatedVisibility(visible = !uiState.isLoading) {
        BottomNavigationBar(
            navController = navController,
            startDestination = startDestination,
            isInvitation = isInvitation || uiState.isInvitation
        )
    }
    AnimatedVisibility(visible = uiState.isLoading) {
        Image(
            painter = painterResource(id = R.drawable.ic_dog_cosmos),
            contentDescription = "Dog in cosmos",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidIMCSTheme {
        AppContent(false)
    }
}
