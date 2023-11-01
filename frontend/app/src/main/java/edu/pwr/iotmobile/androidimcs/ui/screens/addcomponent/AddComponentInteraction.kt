package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

interface AddComponentInteraction {
    fun navigateNext()
    fun navigatePrevious()

    companion object {
        fun default(
            viewModel: AddComponentViewModel
        ) = object : AddComponentInteraction {
            override fun navigateNext() {
                viewModel.navigateNext()
            }

            override fun navigatePrevious() {
                viewModel.navigatePrevious()
            }

        }

        fun empty() = object : AddComponentInteraction {
            override fun navigateNext() {}

            override fun navigatePrevious() {}

        }
    }
}