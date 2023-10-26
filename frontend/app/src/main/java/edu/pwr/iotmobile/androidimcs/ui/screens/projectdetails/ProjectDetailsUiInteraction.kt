package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

interface ProjectDetailsUiInteraction {
    fun setSelectedTabIndex(tab: ProjectDetailsViewModel.ProjectTab)

    companion object {
        fun default(
            viewModel: ProjectDetailsViewModel
        ) = object : ProjectDetailsUiInteraction {
            override fun setSelectedTabIndex(tab: ProjectDetailsViewModel.ProjectTab) {
                viewModel.setSelectedTabIndex(tab)
            }

        }
    }
}