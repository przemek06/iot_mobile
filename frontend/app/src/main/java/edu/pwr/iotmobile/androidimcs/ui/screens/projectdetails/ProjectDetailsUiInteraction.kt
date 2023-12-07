package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

interface ProjectDetailsUiInteraction {
    fun setSelectedTabIndex(tab: ProjectDetailsViewModel.ProjectTab)
    fun onTextChangeDashboard(text: String)
    fun addNewDashboard(name: String)
    fun toggleAddDashboardDialog()
    fun deleteTopic(id: Int)
    fun deleteProject()
    fun toggleDeleteProjectDialog()
    fun toggleLeaveProjectDialog()
    fun regenerateConnectionKey()
    fun leaveGroup()

    companion object {
        fun default(
            viewModel: ProjectDetailsViewModel
        ) = object : ProjectDetailsUiInteraction {
            override fun setSelectedTabIndex(tab: ProjectDetailsViewModel.ProjectTab) {
                viewModel.setSelectedTabIndex(tab)
            }

            override fun onTextChangeDashboard(text: String) {
                viewModel.onTextChangeDashboard(text)
            }

            override fun addNewDashboard(name: String) {
                viewModel.addDashboard(name)
            }

            override fun toggleAddDashboardDialog() {
                viewModel.toggleAddDashboardDialog()
            }

            override fun deleteTopic(id: Int) {
                viewModel.deleteTopic(id)
            }

            override fun deleteProject() {
                viewModel.deleteProject()
            }

            override fun toggleDeleteProjectDialog() {
                viewModel.toggleDeleteProjectDialog()
            }

            override fun toggleLeaveProjectDialog() {
                viewModel.toggleLeaveProjectDialog()
            }

            override fun regenerateConnectionKey() {
                viewModel.regenerateConnectionKey()
            }

            override fun leaveGroup() {
                viewModel.leaveGroup()
            }
        }
    }
}