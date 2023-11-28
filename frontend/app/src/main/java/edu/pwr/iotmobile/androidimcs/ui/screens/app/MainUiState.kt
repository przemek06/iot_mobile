package edu.pwr.iotmobile.androidimcs.ui.screens.app

data class MainUiState(
    val isUserLoggedIn: Boolean = false,
    val isInvitation: Boolean = false,
    val isLoading: Boolean = true
)