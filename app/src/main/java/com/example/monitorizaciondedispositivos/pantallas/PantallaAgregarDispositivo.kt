package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.monitorizaciondedispositivos.modelos.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarDispositivo(navController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()

    // Variables de estado para los campos
    var nombre by remember { mutableStateOf("") }
    var tipoDispositivo by remember { mutableStateOf("Selecciona el tipo de dispositivo") }
    var expanded by remember { mutableStateOf(false) }

    // Variables para los atributos según el tipo de dispositivo
    var rangoTemperatura by remember { mutableStateOf("") }
    var rangoHumedad by remember { mutableStateOf("") }
    var distanciaDeteccion by remember { mutableStateOf("") }
    var anguloDeteccion by remember { mutableStateOf("") }
    var tipoPuerta by remember { mutableStateOf("") }
    var sensibilidad by remember { mutableStateOf("") }
    var capacidadCorriente by remember { mutableStateOf("") }
    var voltajeSoportado by remember { mutableStateOf("") }
    var resolucion by remember { mutableStateOf("") }
    var visionNocturna by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Dispositivo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Desplegable para seleccionar el tipo de dispositivo
            Text("Selecciona el tipo de dispositivo", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown para seleccionar el tipo de dispositivo
            Box {
                Button(onClick = { expanded = true }) {
                    Text(tipoDispositivo)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf(
                        "Sensor de Temperatura y Humedad",
                        "Sensor de Movimiento",
                        "Sensor de Apertura",
                        "Relé Inteligente",
                        "Cámara IP"
                    ).forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                tipoDispositivo = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para el nombre del dispositivo
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del dispositivo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar los campos según el tipo de dispositivo seleccionado
            when (tipoDispositivo) {
                "Sensor de Temperatura y Humedad" -> {
                    OutlinedTextField(
                        value = rangoTemperatura,
                        onValueChange = { rangoTemperatura = it },
                        label = { Text("Rango Temperatura") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = rangoHumedad,
                        onValueChange = { rangoHumedad = it },
                        label = { Text("Rango Humedad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "Sensor de Movimiento" -> {
                    OutlinedTextField(
                        value = distanciaDeteccion,
                        onValueChange = { distanciaDeteccion = it },
                        label = { Text("Distancia de Detección (m)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = anguloDeteccion,
                        onValueChange = { anguloDeteccion = it },
                        label = { Text("Ángulo de Detección (°)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "Sensor de Apertura" -> {
                    OutlinedTextField(
                        value = tipoPuerta,
                        onValueChange = { tipoPuerta = it },
                        label = { Text("Tipo de Puerta") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = sensibilidad,
                        onValueChange = { sensibilidad = it },
                        label = { Text("Sensibilidad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "Relé Inteligente" -> {
                    OutlinedTextField(
                        value = capacidadCorriente,
                        onValueChange = { capacidadCorriente = it },
                        label = { Text("Capacidad Corriente (A)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = voltajeSoportado,
                        onValueChange = { voltajeSoportado = it },
                        label = { Text("Voltaje Soportado (V)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "Cámara IP" -> {
                    OutlinedTextField(
                        value = resolucion,
                        onValueChange = { resolucion = it },
                        label = { Text("Resolución") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = visionNocturna,
                            onCheckedChange = { visionNocturna = it }
                        )
                        Text("Visión Nocturna")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar el dispositivo en Firestore
            Button(
                onClick = {
                    val dispositivoMap = when (tipoDispositivo) {
                        "Sensor de Temperatura y Humedad" -> mapOf(
                            "tipo" to "SensorTemperaturaHumedadDB",
                            "nombre" to nombre,
                            "rangoTemperatura" to rangoTemperatura,
                            "rangoHumedad" to rangoHumedad
                        )
                        "Sensor de Movimiento" -> mapOf(
                            "tipo" to "SensorMovimientoDB",
                            "nombre" to nombre,
                            "distanciaDeteccion" to distanciaDeteccion.toIntOrNull(),
                            "anguloDeteccion" to anguloDeteccion.toIntOrNull()
                        )
                        "Sensor de Apertura" -> mapOf(
                            "tipo" to "SensorAperturaDB",
                            "nombre" to nombre,
                            "tipoPuerta" to tipoPuerta,
                            "sensibilidad" to sensibilidad
                        )
                        "Relé Inteligente" -> mapOf(
                            "tipo" to "ReleInteligenteDB",
                            "nombre" to nombre,
                            "capacidadCorriente" to capacidadCorriente.toDoubleOrNull(),
                            "voltajeSoportado" to voltajeSoportado.toDoubleOrNull()
                        )
                        "Cámara IP" -> mapOf(
                            "tipo" to "CamaraIPDB",
                            "nombre" to nombre,
                            "resolucion" to resolucion,
                            "visionNocturna" to visionNocturna
                        )
                        else -> null
                    }

                    dispositivoMap?.let {
                        firestore.collection("dispositivos").add(it)
                    }

                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
