package edu.pwr.iotmobile.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.pwr.iotmobile.error.exception.SlackException
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import khttp.post as httpPost
@Component
class SlackBot {

    val objectMapper = ObjectMapper()
    fun sendMessageToChannel(webhook: String, message: String) {

        val data = mapOf("text" to message)
        val payload = objectMapper.writeValueAsString(data)

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(webhook))
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode()!=200){
            throw SlackException(response.body())
        }
    }

}