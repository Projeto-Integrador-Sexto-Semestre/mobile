package br.edu.pi.smarthome.data

import br.edu.pi.smarthome.model.CrudSpec
import br.edu.pi.smarthome.model.FieldSpec

object CrudCatalog {
    val items = listOf(
        CrudSpec("users", "Usuario", "Pessoa 1", "/api/users", fields("Nome", "Email", "Status", "Perfil")),
        CrudSpec("profiles", "Perfil/Permissoes", "Pessoa 1", "/api/profiles", fields("Perfil", "Nivel", "Permissoes", "Status")),
        CrudSpec("homes", "Casa", "Pessoa 2", "/api/homes", fields("Casa", "Endereco", "Responsavel", "Status")),
        CrudSpec("rooms", "Comodo", "Pessoa 2", "/api/rooms", fields("Comodo", "Casa", "Andar", "Status")),
        CrudSpec("devices", "Dispositivo IoT", "Pessoa 3", "/api/devices", fields("Dispositivo", "Tipo", "Comodo", "Status")),
        CrudSpec("deviceTypes", "Tipo de Dispositivo", "Pessoa 3", "/api/device-types", fields("Tipo", "Categoria", "Protocolo", "Status")),
        CrudSpec("sensors", "Sensor", "Pessoa 4", "/api/sensors", fields("Sensor", "Tipo", "Topico MQTT", "Status")),
        CrudSpec("sensorReadings", "Leitura de Sensor", "Pessoa 4", "/api/sensor-readings", fields("Sensor", "Valor", "Unidade", "Coletado em")),
        CrudSpec("alerts", "Alerta", "Pessoa 5", "/api/alerts", fields("Titulo", "Tipo", "Prioridade", "Status")),
        CrudSpec("alertTypes", "Tipo de Alerta", "Pessoa 5", "/api/alert-types", fields("Tipo", "Descricao", "Severidade", "Status")),
        CrudSpec("automationRules", "Regra de Automacao", "Pessoa 6", "/api/automation-rules", fields("Regra", "Condicao", "Acao", "Status")),
        CrudSpec("actions", "Acao", "Pessoa 6", "/api/actions", fields("Acao", "Dispositivo", "Comando", "Status")),
        CrudSpec("notifications", "Notificacao", "Pessoa 7", "/api/notifications", fields("Titulo", "Destinatario", "Canal", "Status")),
        CrudSpec("eventLogs", "Log de Eventos", "Pessoa 7", "/api/event-logs", fields("Origem", "Evento", "Nivel", "Criado em"))
    )

    private fun fields(vararg labels: String) = labels.map { label ->
        FieldSpec(label.lowercase().replace(" ", "_"), label)
    }
}
