package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.ui.geometry.Offset

interface DashboardUiInteraction {
    fun setAbsolutePosition(offset: Offset, index: Int)
    fun onComponentClick(index: Int)
    fun setDraggedComponentIndex(index: Int?)
    fun onPlaceDraggedComponent(visibleItems: List<LazyStaggeredGridItemInfo>)

    companion object {
        fun default(
            viewModel: DashboardViewModel
        ) = object : DashboardUiInteraction {
            override fun setAbsolutePosition(offset: Offset, index: Int) {
                viewModel.setAbsolutePosition(offset, index)
            }

            override fun onComponentClick(index: Int) {
                viewModel.onComponentClick(index)
            }

            override fun setDraggedComponentIndex(index: Int?) {
                viewModel.setDraggedComponentIndex(index)
            }

            override fun onPlaceDraggedComponent(visibleItems: List<LazyStaggeredGridItemInfo>) {
                viewModel.onPlaceDraggedComponent(visibleItems)
            }

        }
    }
}