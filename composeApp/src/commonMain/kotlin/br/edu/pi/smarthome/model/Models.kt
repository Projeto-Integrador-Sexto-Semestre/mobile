package br.edu.pi.smarthome.model

data class FieldSpec(
    val name: String,
    val label: String,
    val options: List<String> = emptyList()
)

data class CrudSpec(
    val key: String,
    val label: String,
    val module: String,
    val endpoint: String,
    val fields: List<FieldSpec>
)

data class SmartRecord(
    val id: Int,
    val values: Map<String, String>
)

data class MqttSnapshot(
    val broker: String,
    val topic: String,
    val deviceId: String,
    val temperature: String,
    val gasPpm: String,
    val motion: String
)
