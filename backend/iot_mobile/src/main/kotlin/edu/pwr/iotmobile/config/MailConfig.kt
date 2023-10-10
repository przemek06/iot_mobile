package edu.pwr.iotmobile.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.nio.charset.StandardCharsets


@Configuration
class JavaMailConfig {
    @Value("\${local.application.mail.username}")
    var username: String? = null

    @Value("\${local.application.mail.password}")
    var password: String? = null

    @Value("\${local.application.mail.host}")
    var host: String? = null

    @Value("\${local.application.mail.port}")
    var port: Int? = 587

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port!!
        mailSender.username = username
        mailSender.password = password
        mailSender.defaultEncoding = StandardCharsets.US_ASCII.name()
        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "false"
        return mailSender
    }

}