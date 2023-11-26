package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.ui.geometry.Offset
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto

interface DashboardUiInteraction {
    fun setAbsolutePosition(offset: Offset, id: Int)
    fun onComponentClick(item: ComponentData, value: Any?)
    fun onLocalComponentValueChange(item: ComponentData, value: Any?)
    fun setDraggedComponentId(id: Int?)
    fun onPlaceDraggedComponent(visibleItems: List<LazyStaggeredGridItemInfo>, windowWidth: Float)
    fun onAddNewComponent()

    companion object {
        fun default(
            viewModel: DashboardViewModel,
            openAddComponentScreen: (dto: ComponentListDto) -> Unit
        ) = object : DashboardUiInteraction {
            override fun setAbsolutePosition(offset: Offset, id: Int) {
                viewModel.setAbsolutePosition(offset, id)
            }

            override fun onComponentClick(item: ComponentData, value: Any?) {
                viewModel.onComponentClick(item, value)
            }

            override fun onLocalComponentValueChange(item: ComponentData, value: Any?) {
                viewModel.onLocalComponentValueChange(item, value)
            }

            override fun setDraggedComponentId(id: Int?) {
                viewModel.setDraggedComponentId(id)
            }

            override fun onPlaceDraggedComponent(visibleItems: List<LazyStaggeredGridItemInfo>, windowWidth: Float) {
                viewModel.onPlaceDraggedComponent(visibleItems, windowWidth)
            }

            override fun onAddNewComponent() {
                val dto = viewModel.getComponentListDto() ?: return
                openAddComponentScreen(dto)
            }

        }
    }
}