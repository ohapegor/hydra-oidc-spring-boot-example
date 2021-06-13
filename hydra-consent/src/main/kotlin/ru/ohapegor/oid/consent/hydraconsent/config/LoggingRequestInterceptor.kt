package ru.ohapegor.oid.consent.hydraconsent.config

import mu.KLogging
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.nio.charset.StandardCharsets

class LoggingRequestInterceptor : ClientHttpRequestInterceptor {

    companion object : KLogging()

    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        traceRequest(request, body)
        val response = execution.execute(request, body)
        traceResponse(response)
        return response
    }

    private fun traceRequest(request: HttpRequest, body: ByteArray) {
        logger.debug {
            """
                Request      : ${request.run { "$method $uri" }}
                Headers      : ${request.headers}
                Request body : ${String(body, StandardCharsets.UTF_8)}
            """
        }
    }

    private fun traceResponse(response: ClientHttpResponse) {
        val responseBody = response.body.bufferedReader(StandardCharsets.UTF_8)
                .useLines { it.map(String::trim).joinToString(separator = "") }
        logger.debug {
            """
                Response code: ${response.run { "$statusCode $statusText" }}
                Headers      : ${response.headers}
                Response body: $responseBody
            """
        }
    }
}