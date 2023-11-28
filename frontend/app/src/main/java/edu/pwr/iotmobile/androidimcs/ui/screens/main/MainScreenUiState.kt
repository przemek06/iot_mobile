package edu.pwr.iotmobile.androidimcs.ui.screens.main

import edu.pwr.iotmobile.androidimcs.data.User

data class MainScreenUiState(
    val isInvitation: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)
