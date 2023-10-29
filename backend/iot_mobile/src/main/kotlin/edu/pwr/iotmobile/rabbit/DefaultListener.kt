package edu.pwr.iotmobile.rabbit

import org.hibernate.query.sqm.tree.SqmNode
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class DefaultListener {
    @RabbitListener(id="default-listener", queues = ["tomek"])
    fun receiver(message: String){
        SqmNode.log.info(message)
    }
}