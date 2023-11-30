package edu.pwr.iotmobile.androidimcs.ui.screens.projects

interface ProjectsUiInteraction {

    fun onTextChange(text: String)
    fun addNewProject(name: String)
    fun setDialogVisible()
    fun setDialogInvisible()

    companion object {
        fun default(viewModel: ProjectsViewModel) = object : ProjectsUiInteraction {
            override fun onTextChange(text: String) {
                viewModel.onTextChange(text)
            }
            override fun addNewProject(name: String) {
                viewModel.addProject(name)
            }
            override fun setDialogVisible() {
                viewModel.openDialog()
            }
            override fun setDialogInvisible() {
                viewModel.closeDialog()
            }
        }
    }
}