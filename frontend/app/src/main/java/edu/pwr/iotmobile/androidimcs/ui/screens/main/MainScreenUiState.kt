package edu.pwr.iotmobile.androidimcs.ui.screens.main

import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.data.entity.DashboardEntity

data class MainScreenUiState(
    val isInvitation: Boolean = false,
    val user: User? = null,
    val role: UserRole = UserRole.USER_ROLE,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val dashboards: List<DashboardEntity> = emptyList()
)
