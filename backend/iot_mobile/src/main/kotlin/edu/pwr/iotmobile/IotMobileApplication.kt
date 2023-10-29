package edu.pwr.iotmobile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class IotMobileApplication

fun main(args: Array<String>) {
    runApplication<IotMobileApplication>(*args)
}
