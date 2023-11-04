package edu.pwr.iotmobile.androidimcs.data.scopestates

import edu.pwr.iotmobile.androidimcs.app.koin.AppKoin
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.ScopeID
import java.util.UUID

data class ComponentsListState(
    val componentListDto: ComponentListDto
) {
    companion object {
        fun createScope(componentListDto: ComponentListDto): ScopeID {
            val scopeId: ScopeID = UUID.randomUUID().toString()
            val scope = GlobalContext.get().createScope(scopeId, AppKoin.Scope.COMPONENT_LIST)
            scope.get<ComponentsListState> { parametersOf(componentListDto) }
            return scopeId
        }

        fun getScoped(scopeID: ScopeID): ComponentsListState? {
            val scope = GlobalContext.get().getScopeOrNull(scopeID)
            return scope?.get()
        }
    }
}