package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.ui.geometry.Offset

interface DashboardUiInteraction {
    fun setAbsolutePosition(offset: Offset, id: Int)
    fun onComponentClick(id: Int)
    fun setDraggedComponentId(id: Int?)
    fun onPlaceDraggedComponent(visibleItems: List<LazyStaggeredGridItemInfo>)

    companion object {
        fun default(
            viewModel: DashboardViewModel
        ) = object : DashboardUiInteraction {
            override fun setAbsolutePosition(offset: Offset, id: Int) {
                viewModel.setAbsolutePosition(offset, id)
            }

            override fun onComponentClick(id: Int) {
                viewModel.onComponentClick(id)
            }

            override fun setDraggedComponentId(id: Int?) {
                viewModel.setDraggedComponentId(id)
            }

            override fun onPlaceDraggedComponent(visibleItems: List<LazyStaggeredGridItemInfo>) {
                viewModel.onPlaceDraggedComponent(visibleItems)
            }

        }
    }
}