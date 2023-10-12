package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

interface ProjectDetailsUiInteraction {
    fun setSelectedTabIndex(index: Int)

    companion object {
        fun default(
            viewModel: ProjectDetailsViewModel
        ) = object : ProjectDetailsUiInteraction {
            override fun setSelectedTabIndex(index: Int) {
                viewModel.setSelectedTabIndex(index)
            }

        }
    }
}