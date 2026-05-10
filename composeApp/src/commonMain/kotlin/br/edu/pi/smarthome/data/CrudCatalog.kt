package br.edu.pi.smarthome.data

import br.edu.pi.smarthome.model.CrudSpec
import br.edu.pi.smarthome.model.FieldSpec

object CrudCatalog {
    val items = listOf(
        CrudSpec("users", "Usuario", "Acesso", "/api/users", fields("Nome", "Email", "Status", "Perfil")),
        CrudSpec("profiles", "Perfil/Permissoes", "Acesso", "/api/profiles", fields("Perfil", "Nivel", "Permissoes", "Status")),
        CrudSpec("homes", "Casa", "Moradia", "/api/homes", fields("Casa", "Endereco", "Responsavel", "Status")),
        CrudSpec("rooms", "Comodo", "Moradia", "/api/rooms", fields("Comodo", "Casa", "Andar", "Status")),
        CrudSpec("devices", "Dispositivo IoT", "Dispositivos", "/api/devices", fields("Dispositivo", "Tipo", "Comodo", "Status")),
        CrudSpec("deviceTypes", "Tipo de Dispositivo", "Dispositivos", "/api/device-types", fields("Tipo", "Categoria", "Protocolo", "Status")),
        CrudSpec("sensors", "Sensor", "Sensores", "/api/sensors", fields("Sensor", "Tipo", "Topico MQTT", "Status")),
        CrudSpec("sensorReadings", "Leitura de Sensor", "Sensores", "/api/sensor-readings", fields("Sensor", "Valor", "Unidade", "Coletado em")),
        CrudSpec("alerts", "Alerta", "Alertas", "/api/alerts", fields("Titulo", "Tipo", "Prioridade", "Status")),
        CrudSpec("alertTypes", "Tipo de Alerta", "Alertas", "/api/alert-types", fields("Tipo", "Descricao", "Severidade", "Status")),
        CrudSpec("automationRules", "Regra de Automacao", "Automacao", "/api/automation-rules", fields("Regra", "Condicao", "Acao", "Status")),
        CrudSpec("actions", "Acao", "Automacao", "/api/actions", fields("Acao", "Dispositivo", "Comando", "Status")),
        CrudSpec("notifications", "Notificacao", "Monitoramento", "/api/notifications", fields("Titulo", "Destinatario", "Canal", "Status")),
        CrudSpec("eventLogs", "Log de Eventos", "Monitoramento", "/api/event-logs", fields("Origem", "Evento", "Nivel", "Criado em"))
    )

    private fun fields(vararg labels: String) = labels.map { label ->
        FieldSpec(label.lowercase().replace(" ", "_"), label)
    }
}
