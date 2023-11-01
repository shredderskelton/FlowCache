package com.skelton.flowcache.system

import com.skelton.flowcache.AppConfig
import com.skelton.flowcache.DataResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentLength
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true

    encodeDefaults = true
    isLenient = true
    allowSpecialFloatingPointValues = true
    allowStructuredMapKeys = true
    useArrayPolymorphism = false
}

interface HttpProvider : () -> HttpClient

object HttpProviderDefault : HttpProvider {

    private val http: HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(json)
        }

        defaultRequest {
            url(
                "https://flowcache-8615f-default-rtdb.europe-west1.firebasedatabase.app"
            )
            header(HttpHeaders.Connection, "close")
        }
        expectSuccess = false
    }


    override fun invoke(): HttpClient = http
}

suspend inline fun <reified T> HttpClient.request(block: HttpRequestBuilder.() -> Unit): DataResult<T> {
    val requestBuilder = HttpRequestBuilder()
        .apply {
            // Global config
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        .apply(block)
    delay(AppConfig().simulatedNetworkDelay.toMillis())
    return try {
        val response: HttpResponse = request(requestBuilder)
        if (response.contentLength() ?: 0 < 10) { // null
            DataResult.Error("Not Found", DataResult.Error.Code.NotFound)
        } else if (response.status.value in 200..299) {
            val content: T = response.body<T>()
            DataResult.Success.Network(content)
        } else {
            println("Error calling API: ${requestBuilder.url.encodedPath}: ${response.status.description}")
            DataResult.Error(response.status.description)
        }
    } catch (ex: Throwable) {
        println("Error calling API: ${requestBuilder.url.encodedPath}: ${ex.localizedMessage}")
        DataResult.Error(ex.localizedMessage) // TODO detect different errors
    }
}