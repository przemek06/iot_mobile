package com.example.messagingrabbitmq

import edu.pwr.iotmobile.IotMobileApplication
import edu.pwr.iotmobile.rabbit.Receiver
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Runner(receiver: Receiver, rabbitTemplate: RabbitTemplate) : CommandLineRunner {
    private val rabbitTemplate: RabbitTemplate
    private val receiver: Receiver

    init {
        this.receiver = receiver
        this.rabbitTemplate = rabbitTemplate
    }

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        println("Sending message...")
        rabbitTemplate.convertAndSend(
            "spring-boot-exchange",
            "foo.bar.baz",
            "Hello from RabbitMQ!"
        )

    }
}