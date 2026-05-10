package br.edu.pi.smarthome.data

import br.edu.pi.smarthome.model.MqttSnapshot
import br.edu.pi.smarthome.model.SmartRecord
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface SmartHomeApi {
    suspend fun list(entityKey: String): List<SmartRecord>
    suspend fun create(entityKey: String, values: Map<String, String>): SmartRecord
    suspend fun update(entityKey: String, id: Int, values: Map<String, String>): SmartRecord
    suspend fun delete(entityKey: String, id: Int)
    suspend fun mqttSnapshot(): MqttSnapshot
}

class RestSmartHomeApi(
    private val baseUrl: String = "http://10.0.2.2:8080",
    private val jwtProvider: () -> String? = { null }
) {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    fun endpoint(path: String): String = "$baseUrl$path"
    fun authorizationHeader(): String? = jwtProvider()?.let { "Bearer $it" }
}
