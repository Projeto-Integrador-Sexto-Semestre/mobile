package br.edu.pi.smarthome.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.pi.smarthome.data.CrudCatalog
import br.edu.pi.smarthome.data.MockSmartHomeApi
import br.edu.pi.smarthome.model.CrudSpec
import br.edu.pi.smarthome.model.MqttSnapshot
import br.edu.pi.smarthome.model.SmartRecord
import kotlinx.coroutines.launch

private val Accent = Color(0xFF4F46E5)
private val AccentSoft = Color(0xFFEDEBFF)
private val Cyan = Color(0xFF0891B2)
private val Amber = Color(0xFFF59E0B)
private val Ink = Color(0xFF172033)
private val Muted = Color(0xFF667085)
private val AppBg = Color(0xFFF7F8FB)

@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = AppBg) {
            SmartHomeScreen()
        }
    }
}

@Composable
private fun SmartHomeScreen() {
    val api = remember { MockSmartHomeApi() }
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf(CrudCatalog.items.first()) }
    var records by remember { mutableStateOf(emptyList<SmartRecord>()) }
    var mqtt by remember { mutableStateOf<MqttSnapshot?>(null) }

    fun reload(spec: CrudSpec) {
        scope.launch {
            records = api.list(spec.key)
            mqtt = api.mqttSnapshot()
        }
    }

    LaunchedEffect(Unit) { reload(selected) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Header(mqtt) }
        item { InsightsPanel() }
        item {
            CrudSelector(
                selected = selected,
                onSelect = {
                    selected = it
                    reload(it)
                }
            )
        }
        item {
            CrudPanel(
                spec = selected,
                records = records,
                onCreate = { values ->
                    scope.launch {
                        api.create(selected.key, values)
                        reload(selected)
                    }
                },
                onDelete = { id ->
                    scope.launch {
                        api.delete(selected.key, id)
                        reload(selected)
                    }
                }
            )
        }
    }
}

@Composable
private fun Header(mqtt: MqttSnapshot?) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("PI Smart Home", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("14 CRUDs mockados, API REST preparada e painel IoT via MQTT.", color = Color.White)
            Text("Broker: ${mqtt?.broker ?: "Mosquitto"} | Topico: ${mqtt?.topic ?: "home/+/telemetry"}", color = Color(0xFFD7DDF0))
        }
    }
}

@Composable
private fun InsightsPanel() {
    val modules = CrudCatalog.items.groupBy { it.module }.mapValues { it.value.size }
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Visao geral", fontWeight = FontWeight.Bold, color = Ink)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                MetricCard("CRUDs", "14", Accent, Modifier.weight(1f))
                MetricCard("Modulos", modules.size.toString(), Cyan, Modifier.weight(1f))
                MetricCard("MQTT", "1", Amber, Modifier.weight(1f))
            }
            modules.forEach { (module, count) ->
                ChartRow(label = module, count = count, max = 14)
            }
        }
    }
}

@Composable
private fun MetricCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)), modifier = modifier) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(label, color = Muted)
        }
    }
}

@Composable
private fun ChartRow(label: String, count: Int, max: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Ink, fontWeight = FontWeight.Bold)
            Text("$count CRUDs", color = Muted)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color(0xFFE5E9F2))
        ) {
            Box(
                Modifier
                    .fillMaxWidth(count / max.toFloat())
                    .height(10.dp)
                    .background(Accent)
            )
        }
    }
}

@Composable
private fun CrudSelector(selected: CrudSpec, onSelect: (CrudSpec) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Modulos do sistema", fontWeight = FontWeight.Bold, color = Ink)
            CrudCatalog.items.forEach { spec ->
                val bg = if (spec.key == selected.key) AccentSoft else Color.Transparent
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bg)
                        .clickable { onSelect(spec) }
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(spec.label, color = Ink)
                    Text(spec.module, color = Accent, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CrudPanel(
    spec: CrudSpec,
    records: List<SmartRecord>,
    onCreate: (Map<String, String>) -> Unit,
    onDelete: (Int) -> Unit
) {
    val form = remember(spec.key) { mutableStateMapOf<String, String>() }

    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("CRUD ${spec.label}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Endpoint: ${spec.endpoint}", color = Accent, fontWeight = FontWeight.Bold)

            spec.fields.forEach { field ->
                OutlinedTextField(
                    value = form[field.name].orEmpty(),
                    onValueChange = { form[field.name] = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(field.label) },
                    singleLine = true
                )
            }

            Button(
                onClick = {
                    onCreate(spec.fields.associate { it.name to form[it.name].orEmpty().ifBlank { it.label } })
                    form.clear()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Accent)
            ) {
                Text("Salvar mock")
            }

            Spacer(Modifier.height(4.dp))
            Text("Registros", fontWeight = FontWeight.Bold)
            records.forEach { record ->
                RecordCard(spec, record, onDelete)
            }
        }
    }
}

@Composable
private fun RecordCard(spec: CrudSpec, record: SmartRecord, onDelete: (Int) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = AccentSoft)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("#${record.id}", color = Accent, fontWeight = FontWeight.Bold)
            spec.fields.forEach { field ->
                Text("${field.label}: ${record.values[field.name].orEmpty()}", color = Ink)
            }
            Button(
                onClick = { onDelete(record.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
            ) {
                Text("Excluir")
            }
        }
    }
}
