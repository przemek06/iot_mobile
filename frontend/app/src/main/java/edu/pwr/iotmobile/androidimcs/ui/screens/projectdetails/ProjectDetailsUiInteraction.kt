package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import edu.pwr.iotmobile.androidimcs.data.TopicDataType

interface ProjectDetailsUiInteraction {
    fun setSelectedTabIndex(tab: ProjectDetailsViewModel.ProjectTab)
    fun setDialogVisible()
    fun setDialogInvisible()
    fun setInfoVisible()
    fun setInfoInvisible()
    fun onTextChangeDashboard(text: String)
    fun addNewDashboard(name: String)
    fun addNewTopic(name: String, uniqueName: String, dataType: TopicDataType)

    companion object {
        fun default(
            viewModel: ProjectDetailsViewModel
        ) = object : ProjectDetailsUiInteraction {
            override fun setSelectedTabIndex(tab: ProjectDetailsViewModel.ProjectTab) {
                viewModel.setSelectedTabIndex(tab)
            }

            override fun setDialogVisible() {
                viewModel.setDialogVisible()
            }
            override fun setDialogInvisible() {
                viewModel.setDialogInvisible()
            }

            override fun setInfoVisible() {
                viewModel.setInfoVisible()
            }
            override fun setInfoInvisible() {
                viewModel.setInfoInvisible()
            }

            override fun onTextChangeDashboard(text: String) {
                viewModel.onTextChangeDashboard(text)
            }

            override fun addNewDashboard(name: String) {
                viewModel.addDashboard(name)
            }
            override fun addNewTopic(name: String, uniqueName: String, dataType: TopicDataType) {
                viewModel.addTopic(name,uniqueName, dataType)
            }
        }
    }
}