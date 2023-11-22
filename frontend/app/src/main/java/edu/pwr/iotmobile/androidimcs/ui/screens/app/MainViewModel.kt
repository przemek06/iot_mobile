package edu.pwr.iotmobile.androidimcs.ui.screens.app

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationAlertDto
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.listener.InvitationAlertWebSocketListener
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class MainViewModel(
    private val userRepository: UserRepository,
    private val client: OkHttpClient,
    val toast: Toast,
    val event: Event
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private var invitationAlertWebSocketListener: InvitationAlertWebSocketListener? = null

    init {
        viewModelScope.launch(Dispatchers.Default) {
            userRepository.getLoggedInUser().collect { user ->
                _uiState.update {
                    it.copy(isUserLoggedIn = user != null)
                }

                if (user != null) {
                    // User has logged in
                    invitationAlertWebSocketListener?.closeWebSocket()
                    invitationAlertWebSocketListener = InvitationAlertWebSocketListener(
                        client = client,
                        onNewInvitation = { data -> onNewInvitation(data) }
                    )
                    // TODO: navigate to main screen
                } else {
                    // User has been logged out
                    invitationAlertWebSocketListener?.closeWebSocket()
                    // TODO: navigate to login screen
                }
            }
        }
    }

    // TODO: delete if registerLauncher works
    fun handleIntent(intent: Intent?) {
        if (intent == null) return

        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            val path: String = appLinkData.path ?: return
            val id = path.substring(path.lastIndexOf('/') + 1)
            viewModelScope.launch {
                event.event(id)
            }
        }
    }

    private fun onNewInvitation(data: InvitationAlertDto) {
        viewModelScope.launch {
            // TODO: snackbar
            toast.toast("You have received a new invitation!")
        }
    }
}