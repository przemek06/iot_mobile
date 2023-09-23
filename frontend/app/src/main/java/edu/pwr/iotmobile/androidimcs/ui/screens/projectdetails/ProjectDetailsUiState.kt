package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

data class ProjectDetailsUiState(
    val selectedTabIndex: Int,
    val dashboards: List<Any>,
    val topics: List<Any>
) {
    companion object {
        fun default(
            selectedTabIndex: Int = 0,
            dashboards: List<Any> = emptyList(),
            topics: List<Any> = emptyList()
        ) = ProjectDetailsUiState(
            selectedTabIndex = selectedTabIndex,
            dashboards = dashboards,
            topics = topics
        )
    }
}