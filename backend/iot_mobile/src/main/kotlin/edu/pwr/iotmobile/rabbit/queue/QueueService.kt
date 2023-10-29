package edu.pwr.iotmobile.rabbit.queue

import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint
import org.springframework.stereotype.Service
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer

@Service
@Slf4j
class QueueService(
    val rabbitAdmin: RabbitAdmin,
    val rabbitTemplate: RabbitTemplate,
    val rabbitListenerEndpointRegistry: RabbitListenerEndpointRegistry
) {
    /**
     * send message to default DIRECT exchange using explicit
     * queueName and message value
     */
    fun sendMessage(queueName: String, message: String){
        log.info("Mess" )
        log.info( rabbitListenerEndpointRegistry.listenerContainers.size)
        rabbitTemplate.convertAndSend("", queueName, message)
    }

    /**
     * add new queue with binding to default exchange and attach to default listener
     */
    fun addQueue(queueName: String){

        val queue = Queue(queueName, true, false, false)
        val binging = Binding(queueName, Binding.DestinationType.QUEUE, "", queueName, null)
        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareBinding(binging)
//        addQueueToListener("default-listener", queue)
    }

    fun addTestQueue(queueName: String){
        addQueue(queueName)
        val listenerContainer = SimpleMessageListenerContainer(rabbitTemplate.connectionFactory)
        listenerContainer.setQueueNames(queueName)

        val listener = MessageListener { data -> processMessage(data) }
        listenerContainer.setMessageListener(listener)

        val endpoint = SimpleRabbitListenerEndpoint()
        endpoint.id = queueName
        endpoint.messageListener = listener

        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(rabbitTemplate.connectionFactory)
        rabbitListenerEndpointRegistry.registerListenerContainer(endpoint, factory, true
        )

    }

    fun processMessage(data: Message) {
        log.info(String(data.body))
    }
    /**
     * attach new queue to listener
     */
//    fun addQueueToListener(listenerId: String, queue: Queue){
//        getListenerById(listenerId).addQueues(queue)
//    }

//    fun getListenerById(listenerId: String): AbstractMessageListenerContainer {
////        if (rabbitListenerEndpointRegistry.listenerContainerIds.contains(listenerId)) {
//            return rabbitListenerEndpointRegistry.getListenerContainer(listenerId) as AbstractMessageListenerContainer
////        }
//
//    }





}