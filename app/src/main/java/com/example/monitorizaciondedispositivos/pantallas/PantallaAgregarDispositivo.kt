package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore

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
    var tipoValvula by remember { mutableStateOf("") }
    var presionMaxima by remember { mutableStateOf("") }
    var soporteHVAC by remember { mutableStateOf(false) }
    var capacidadBTU by remember { mutableStateOf("") }
    var sensores by remember { mutableStateOf("") }
    var rangoOperacion by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Dispositivo", fontSize = 20.sp) },
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
                .background(Color(0xFFF5F5F5)) // Color de fondo más limpio
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Selecciona el tipo de dispositivo", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown para seleccionar el tipo de dispositivo
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(tipoDispositivo, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf(
                        "Sensor de Temperatura y Humedad",
                        "Sensor de Movimiento",
                        "Sensor de Apertura",
                        "Relé Inteligente",
                        "Actuador de Válvula",
                        "Servomotor",
                        "Cámara IP",
                        "Controlador de Clima",
                        "Estación Meteorológica"
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar los campos según el tipo de dispositivo seleccionado
            when (tipoDispositivo) {
                "Sensor de Temperatura y Humedad" -> {
                    OutlinedTextField(value = rangoTemperatura, onValueChange = { rangoTemperatura = it }, label = { Text("Rango Temperatura") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = rangoHumedad, onValueChange = { rangoHumedad = it }, label = { Text("Rango Humedad") }, modifier = Modifier.fillMaxWidth())
                }
                "Sensor de Movimiento" -> {
                    OutlinedTextField(value = distanciaDeteccion, onValueChange = { distanciaDeteccion = it }, label = { Text("Distancia de Detección (m)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = anguloDeteccion, onValueChange = { anguloDeteccion = it }, label = { Text("Ángulo de Detección (°)") }, modifier = Modifier.fillMaxWidth())
                }
                "Sensor de Apertura" -> {
                    OutlinedTextField(value = tipoPuerta, onValueChange = { tipoPuerta = it }, label = { Text("Tipo de Puerta") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = sensibilidad, onValueChange = { sensibilidad = it }, label = { Text("Sensibilidad") }, modifier = Modifier.fillMaxWidth())
                }
                "Relé Inteligente" -> {
                    OutlinedTextField(value = capacidadCorriente, onValueChange = { capacidadCorriente = it }, label = { Text("Capacidad Corriente (A)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = voltajeSoportado, onValueChange = { voltajeSoportado = it }, label = { Text("Voltaje Soportado (V)") }, modifier = Modifier.fillMaxWidth())
                }
                "Actuador de Válvula" -> {
                    OutlinedTextField(value = tipoValvula, onValueChange = { tipoValvula = it }, label = { Text("Tipo de Válvula") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = presionMaxima, onValueChange = { presionMaxima = it }, label = { Text("Presión Máxima") }, modifier = Modifier.fillMaxWidth())
                }
                "Cámara IP" -> {
                    OutlinedTextField(value = resolucion, onValueChange = { resolucion = it }, label = { Text("Resolución") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Visión Nocturna")
                        Switch(checked = visionNocturna, onCheckedChange = { visionNocturna = it })
                    }
                }
                "Controlador de Clima" -> {
                    OutlinedTextField(value = capacidadBTU, onValueChange = { capacidadBTU = it }, label = { Text("Capacidad BTU") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Soporta HVAC")
                        Switch(checked = soporteHVAC, onCheckedChange = { soporteHVAC = it })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text("Guardar", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
