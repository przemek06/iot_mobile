package edu.pwr.iotmobile.androidimcs.ui.screens.projects

data class ProjectsUiState(
    val projects: List<Any>
) {
    companion object {
        fun default(
            projects: List<Any> = emptyList()
        ) = ProjectsUiState(
            projects = projects
        )
    }
}