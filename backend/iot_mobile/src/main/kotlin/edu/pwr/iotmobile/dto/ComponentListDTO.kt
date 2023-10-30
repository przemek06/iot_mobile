package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Component
import jakarta.validation.constraints.NotNull

data class ComponentListDTO(
    @NotNull
    val dashboardId: Int,
    @NotNull
    val components: List<ComponentDTO>
) {
    fun toEntityList() : List<Component> {
        return components.map { it.toEntity(dashboardId) }
    }
}
