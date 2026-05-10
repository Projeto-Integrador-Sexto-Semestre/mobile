package br.edu.pi.smarthome.data

import br.edu.pi.smarthome.model.MqttSnapshot
import br.edu.pi.smarthome.model.SmartRecord

class MockSmartHomeApi : SmartHomeApi {
    private val database = CrudCatalog.items.associate { spec ->
        spec.key to mutableListOf(
            SmartRecord(1, spec.fields.associate { it.name to sampleValue(spec.label, it.label, 1) }),
            SmartRecord(2, spec.fields.associate { it.name to sampleValue(spec.label, it.label, 2) })
        )
    }.toMutableMap()

    override suspend fun list(entityKey: String): List<SmartRecord> {
        return database[entityKey].orEmpty()
    }

    override suspend fun create(entityKey: String, values: Map<String, String>): SmartRecord {
        val collection = database.getOrPut(entityKey) { mutableListOf() }
        val record = SmartRecord((collection.maxOfOrNull { it.id } ?: 0) + 1, values)
        collection.add(0, record)
        return record
    }

    override suspend fun update(entityKey: String, id: Int, values: Map<String, String>): SmartRecord {
        val collection = database.getOrPut(entityKey) { mutableListOf() }
        val index = collection.indexOfFirst { it.id == id }
        val updated = SmartRecord(id, collection.getOrNull(index)?.values.orEmpty() + values)
        if (index >= 0) collection[index] = updated
        return updated
    }

    override suspend fun delete(entityKey: String, id: Int) {
        database[entityKey]?.removeAll { it.id == id }
    }

    override suspend fun mqttSnapshot() = MqttSnapshot(
        broker = "Mosquitto",
        topic = "home/+/telemetry",
        deviceId = "esp32-cozinha",
        temperature = "26.4 C",
        gasPpm = "118 ppm",
        motion = "false"
    )

    private fun sampleValue(entity: String, field: String, index: Int): String {
        return when {
            field.contains("Status", ignoreCase = true) -> if (index == 1) "Ativo" else "Online"
            field.contains("Email", ignoreCase = true) -> "usuario$index@email.com"
            field.contains("Topico", ignoreCase = true) -> "home/sala/telemetry"
            field.contains("Valor", ignoreCase = true) -> "26.$index"
            else -> "$entity $index"
        }
    }
}
