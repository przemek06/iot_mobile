package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.User
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MailService(private val templateEngine: TemplateEngine, private val mailSender: JavaMailSender) {

    private fun generateMailHtml(htmlTemplate: String, vararg variables: Pair<String, String>): String {
        val context = Context()
        variables.forEach {
            context.setVariable(it.first, it.second)
        }
        return templateEngine.process(htmlTemplate, context)
    }

    private fun sendHtmlMail(subject: String, address: String, html: String) {
        val simpleMailMessage: MimeMessage = mailSender.createMimeMessage()
        simpleMailMessage.subject = subject
        val helper = MimeMessageHelper(simpleMailMessage, false)
        helper.setTo(address)
        helper.setText(html, true)
        mailSender.send(simpleMailMessage)
    }

    @Async
    fun sendUserVerificationMail(user: User, code: String) {
        val html = generateMailHtml(
            "verification_mail",
            Pair("name", user.name),
            Pair("code", code)
        )
        sendHtmlMail("Account activation", user.email, html)
    }

    @Async
    fun sendResetPasswordMail(user: User, code: String) {
        val html = generateMailHtml("reset_password_mail", Pair("name", user.name), Pair("code", code))
        sendHtmlMail("Reset Password", user.email, html)
    }
}