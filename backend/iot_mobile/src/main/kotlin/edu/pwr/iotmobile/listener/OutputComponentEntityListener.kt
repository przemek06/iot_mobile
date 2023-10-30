package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.entities.OutputComponent
import edu.pwr.iotmobile.service.ComponentChangeService
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class OutputComponentEntityListener {

    @Autowired
    private lateinit var componentChangeService: ComponentChangeService

    @PostPersist
    private fun afterInsert(entity: OutputComponent) {
        // Entity creation logic
    }

    @PostUpdate
    fun afterUpdate(entity: OutputComponent) {
        // Entity update logic
    }

    @PostRemove
    fun afterDelete(entity: OutputComponent) {
        // Entity deletion logic
    }

}