package edu.pwr.iotmobile.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.pwr.iotmobile.error.exception.TelegramException
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class TelegramBot {

    val uriPattern = "https://api.telegram.org/bot{token}/sendMessage"
    val objectMapper = ObjectMapper()
    fun sendMessageToChannel(token: String, message: String) {

        val chatId = getChatId(token)
        val botToken = getBotToken(token)
        val uri = createURI(botToken)

        val data = mapOf("text" to message, "chat_id" to chatId)
        val payload = objectMapper.writeValueAsString(data)

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .header("Content-Type", "application/json")
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw TelegramException(response.body())
        }
    }

    fun getBotToken(token: String): String {
        return token.split(";")[1]
    }

    fun getChatId(token: String): String {
        return token.split(";")[0]
    }

    fun createURI(botToken: String): String {
        return uriPattern.replace("{token}", botToken)
    }
}