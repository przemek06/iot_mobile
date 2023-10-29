package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.service.ComponentChangeService
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TriggerComponentEntityListener {

    @Autowired
    private lateinit var componentChangeService: ComponentChangeService

    @PostPersist
    private fun afterInsert(entity: TriggerComponent) {
        // Entity creation logic
    }

    @PostUpdate
    fun afterUpdate(entity: TriggerComponent) {
        // Entity update logic
    }

    @PostRemove
    fun afterDelete(entity: TriggerComponent) {
        // Entity deletion logic
    }

}