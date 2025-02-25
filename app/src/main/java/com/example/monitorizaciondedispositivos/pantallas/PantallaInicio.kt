package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.monitorizaciondedispositivos.data.AuthViewModel
import com.example.monitorizaciondedispositivos.modelos.*
import com.example.monitorizaciondedispositivos.mqtt.MqttManager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(navController: NavHostController, authViewModel: AuthViewModel) {
    val firestore = FirebaseFirestore.getInstance()
    var dispositivos by remember { mutableStateOf(listOf<DispositivoBD>()) }
    var showDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val dispositivoDatos = remember { mutableStateMapOf<String, String>() }
    var sinRegistros by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(refreshTrigger) {
        isLoading = true
        try {
            firestore.collection("dispositivos")
                .get()
                .addOnSuccessListener { result ->
                    Log.d("PantallaInicio", "Documentos recuperados: ${result.size()}")
                    dispositivos = result.documents.mapNotNull { document ->
                        try {
                            when (document.getString("tipo")) {
                                "SensorTemperaturaHumedadDB" -> document.toObject(SensorTemperaturaHumedadDB::class.java)
                                "SensorMovimientoDB" -> document.toObject(SensorMovimientoDB::class.java)
                                "SensorAperturaDB" -> document.toObject(SensorAperturaDB::class.java)
                                "ReleInteligenteDB" -> document.toObject(ReleInteligenteDB::class.java)
                                "ActuadorValvulaDB" -> document.toObject(ActuadorValvulaDB::class.java)
                                "ServomotorDB" -> document.toObject(ServomotorDB::class.java)
                                "CamaraIPDB" -> document.toObject(CamaraIPDB::class.java)
                                "ControladorClimaDB" -> document.toObject(ControladorClimaDB::class.java)
                                "EstacionMeteorologicaDB" -> document.toObject(EstacionMeteorologicaDB::class.java)
                                "SensorCalidadAireDB" -> document.toObject(SensorCalidadAireDB::class.java)
                                "SensorInundacionDB" -> document.toObject(SensorInundacionDB::class.java)
                                "PanelSolarDB" -> document.toObject(PanelSolarDB::class.java)
                                "SensorPresionDB" -> document.toObject(SensorPresionDB::class.java)
                                "SensorLuzDB" -> document.toObject(SensorLuzDB::class.java)
                                else -> {
                                    Log.w("PantallaInicio", "Tipo de dispositivo desconocido: ${document.getString("tipo")}")
                                    null
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("PantallaInicio", "Error al convertir documento: ${e.message}")
                            e.printStackTrace()
                            null
                        }
                    }
                    sinRegistros = dispositivos.isEmpty()
                    isLoading = false
                    Log.d("PantallaInicio", "Dispositivos cargados: ${dispositivos.size}")
                }
                .addOnFailureListener { e ->
                    Log.e("PantallaInicio", "Error al cargar dispositivos", e)
                    error = e.message
                    isLoading = false
                }
        } catch (e: Exception) {
            Log.e("PantallaInicio", "Error general", e)
            error = e.message
            isLoading = false
        }
    }

    DisposableEffect(Unit) {
        val topics = dispositivos.mapNotNull { it.topic }.distinct()
        MqttManager.connect(topics) { topic, message ->
            val newState = message == "ON"
            firestore.collection("dispositivos")
                .whereEqualTo("topic", topic)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        firestore.collection("dispositivos")
                            .document(document.id)
                            .update("estado", newState)
                            .addOnSuccessListener {
                                refreshTrigger += 1
                            }
                    }
                }
        }
        onDispose {
            MqttManager.disconnect()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Dispositivos") },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("pantalla_agregar_dispositivo") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar dispositivo")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Text(
                        text = "Error: $error",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                sinRegistros -> {
                    Text(
                        text = "No hay dispositivos registrados",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(dispositivos) { dispositivo ->
                            DispositivoCard(
                                dispositivo = dispositivo,
                                onStateChange = { newState ->
                                    dispositivo.topic?.let { topic ->
                                        MqttManager.publish(topic, if (newState) "ON" else "OFF")
                                        firestore.collection("dispositivos")
                                            .whereEqualTo("topic", topic)
                                            .get()
                                            .addOnSuccessListener { result ->
                                                for (document in result) {
                                                    firestore.collection("dispositivos")
                                                        .document(document.id)
                                                        .update("estado", newState)
                                                        .addOnSuccessListener {
                                                            refreshTrigger += 1
                                                        }
                                                }
                                            }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DispositivoCard(
    dispositivo: DispositivoBD,
    onStateChange: (Boolean) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar este dispositivo?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        firestore.collection("dispositivos")
                            .whereEqualTo("topic", dispositivo.topic)
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    document.reference.delete()
                                }
                            }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dispositivo.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar dispositivo",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Switch(
                        checked = dispositivo.estado,
                        onCheckedChange = onStateChange
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Mostrar detalles específicos según el tipo de dispositivo
            when (dispositivo) {
                is SensorTemperaturaHumedadDB -> {
                    Text("Rango de Temperatura: ${dispositivo.rangoTemperatura}")
                    Text("Rango de Humedad: ${dispositivo.rangoHumedad}")
                }
                is SensorMovimientoDB -> {
                    Text("Distancia de Detección: ${dispositivo.distanciaDeteccion}m")
                    Text("Ángulo de Detección: ${dispositivo.anguloDeteccion}°")
                }
                is SensorAperturaDB -> {
                    Text("Tipo de Puerta: ${dispositivo.tipoPuerta}")
                    Text("Sensibilidad: ${dispositivo.sensibilidad}")
                }
                is ReleInteligenteDB -> {
                    Text("Capacidad de Corriente: ${dispositivo.capacidadCorriente}A")
                    Text("Voltaje Soportado: ${dispositivo.voltajeSoportado}V")
                }
                is SensorCalidadAireDB -> {
                    Text("Nivel CO2: ${dispositivo.nivelCO2} ppm")
                    Text("Nivel VOC: ${dispositivo.nivelVOC}")
                    Text("Calidad del Aire: ${dispositivo.calidadAire}")
                }
                is SensorInundacionDB -> {
                    Text("Nivel de Sensibilidad: ${dispositivo.nivelSensibilidad}")
                    Text("Área de Cobertura: ${dispositivo.areaCobertura}m²")
                    Text("Tipo de Sensor: ${dispositivo.tipoSensor}")
                }
                else -> {
                    if (dispositivo.topic != null) {
                        Text("Topic MQTT: ${dispositivo.topic}")
                    }
                }
            }
            
            if (dispositivo.topic != null) {
                Text(
                    text = "Estado MQTT: ${if (dispositivo.estado) "Conectado" else "Desconectado"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (dispositivo.estado) Color.Green else Color.Red
                )
            }
        }
    }
}
