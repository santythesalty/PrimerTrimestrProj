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
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(navController: NavHostController, authViewModel: AuthViewModel) {
    val dispositivos = remember { mutableStateListOf<DispositivoBD>() }
    val firestore = FirebaseFirestore.getInstance()
    var sinRegistros by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        firestore.collection("dispositivos").addSnapshotListener { snapshot, _ ->
            dispositivos.clear()
            if (snapshot == null || snapshot.isEmpty) {
                sinRegistros = true
            } else {
                sinRegistros = false
                snapshot.documents.forEach { document ->
                    val tipo = document.getString("tipo") ?: ""
                    val nombre = document.getString("nombre") ?: "Desconocido"

                    val dispositivo = when (tipo) {
                        "SensorTemperaturaHumedadDB" -> SensorTemperaturaHumedadDB(
                            nombre = nombre,
                            rangoTemperatura = document.getString("rangoTemperatura") ?: "",
                            rangoHumedad = document.getString("rangoHumedad") ?: ""
                        )
                        "SensorMovimientoDB" -> SensorMovimientoDB(
                            nombre = nombre,
                            distanciaDeteccion = document.getLong("distanciaDeteccion")?.toInt() ?: 0,
                            anguloDeteccion = document.getLong("anguloDeteccion")?.toInt() ?: 0
                        )
                        "CamaraIPDB" -> CamaraIPDB(
                            nombre = nombre,
                            resolucion = document.getString("resolucion") ?: "",
                            visionNocturna = document.getBoolean("visionNocturna") ?: false
                        )
                        "SensorCalidadAireDB" -> SensorCalidadAireDB(
                            nombre = nombre,
                            nivelCO2 = document.getLong("nivelCO2")?.toInt() ?: 0,
                            nivelVOC = document.getLong("nivelVOC")?.toInt() ?: 0,
                            calidadAire = document.getString("calidadAire") ?: ""
                        )
                        "SensorInundacionDB" -> SensorInundacionDB(
                            nombre = nombre,
                            nivelSensibilidad = document.getString("nivelSensibilidad") ?: "",
                            areaCobertura = document.getDouble("areaCobertura") ?: 0.0,
                            tipoSensor = document.getString("tipoSensor") ?: ""
                        )
                        "PanelSolarDB" -> PanelSolarDB(
                            nombre = nombre,
                            potenciaMaxima = document.getLong("potenciaMaxima")?.toInt() ?: 0,
                            eficiencia = document.getDouble("eficiencia") ?: 0.0,
                            orientacion = document.getString("orientacion") ?: ""
                        )
                        "SensorPresionDB" -> SensorPresionDB(
                            nombre = nombre,
                            rangoPresion = document.getString("rangoPresion") ?: "",
                            precision = document.getDouble("precision") ?: 0.0,
                            unidadMedida = document.getString("unidadMedida") ?: ""
                        )
                        "SensorLuzDB" -> SensorLuzDB(
                            nombre = nombre,
                            rangoLuminosidad = document.getString("rangoLuminosidad") ?: "",
                            tipoLuz = document.getString("tipoLuz") ?: "",
                            sensibilidadEspectral = document.getString("sensibilidadEspectral") ?: ""
                        )
                        else -> null
                    }

                    dispositivo?.let { dispositivos.add(it) }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Lista de Dispositivos",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("pantalla_inicio") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("pantalla_agregar_dispositivo") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", color = Color.White, fontSize = 24.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (sinRegistros) {
                Text("SIN REGISTROS", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(dispositivos) { dispositivo ->
                        DispositivoCard(dispositivo, firestore)
                    }
                }
            }
        }
    }
}

@Composable
fun DispositivoCard(dispositivo: DispositivoBD, firestore: FirebaseFirestore) {
    var showDialog by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) }

    val icono = when (dispositivo) {
        is SensorTemperaturaHumedadDB -> Icons.Default.Thermostat
        is SensorMovimientoDB -> Icons.Default.DirectionsRun
        is CamaraIPDB -> Icons.Default.Videocam
        is SensorCalidadAireDB -> Icons.Default.AirplanemodeActive
        is SensorInundacionDB -> Icons.Default.WaterDrop
        is PanelSolarDB -> Icons.Default.WbSunny
        is SensorPresionDB -> Icons.Default.Speed
        is SensorLuzDB -> Icons.Default.Lightbulb
        else -> Icons.Default.Devices
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icono, contentDescription = "Ícono del dispositivo", tint = Color.Blue, modifier = Modifier.size(40.dp))

            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(text = "Nombre: ${dispositivo.nombre}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                when (dispositivo) {
                    is SensorTemperaturaHumedadDB -> {
                        Text("Rango Temperatura: ${dispositivo.rangoTemperatura}")
                        Text("Rango Humedad: ${dispositivo.rangoHumedad}")
                    }
                    is SensorMovimientoDB -> {
                        Text("Distancia Detección: ${dispositivo.distanciaDeteccion}m")
                        Text("Ángulo Detección: ${dispositivo.anguloDeteccion}°")
                    }
                    is CamaraIPDB -> {
                        Text("Resolución: ${dispositivo.resolucion}")
                        Text("Visión Nocturna: ${if (dispositivo.visionNocturna) "Sí" else "No"}")
                    }
                    is SensorCalidadAireDB -> {
                        Text("Nivel CO2: ${dispositivo.nivelCO2} ppm")
                        Text("Nivel VOC: ${dispositivo.nivelVOC} ppb")
                        Text("Calidad del Aire: ${dispositivo.calidadAire}")
                    }
                    is SensorInundacionDB -> {
                        Text("Sensibilidad: ${dispositivo.nivelSensibilidad}")
                        Text("Área de Cobertura: ${dispositivo.areaCobertura}m²")
                        Text("Tipo de Sensor: ${dispositivo.tipoSensor}")
                    }
                    is PanelSolarDB -> {
                        Text("Potencia Máxima: ${dispositivo.potenciaMaxima}W")
                        Text("Eficiencia: ${dispositivo.eficiencia}%")
                        Text("Orientación: ${dispositivo.orientacion}")
                    }
                    is SensorPresionDB -> {
                        Text("Rango de Presión: ${dispositivo.rangoPresion}")
                        Text("Precisión: ${dispositivo.precision}")
                        Text("Unidad: ${dispositivo.unidadMedida}")
                    }
                    is SensorLuzDB -> {
                        Text("Rango Luminosidad: ${dispositivo.rangoLuminosidad}")
                        Text("Tipo de Luz: ${dispositivo.tipoLuz}")
                        Text("Sensibilidad Espectral: ${dispositivo.sensibilidadEspectral}")
                    }
                    is ActuadorValvulaDB -> TODO()
                    is ControladorClimaDB -> TODO()
                    is EstacionMeteorologicaDB -> TODO()
                    is ReleInteligenteDB -> TODO()
                    is SensorAperturaDB -> TODO()
                    is ServomotorDB -> TODO()
                }
            }

            Box {
                IconButton(onClick = { expandedMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menú opciones")
                }
                DropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Borrar") },
                        onClick = {
                            showDialog = true
                            expandedMenu = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este dispositivo? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        firestore.collection("dispositivos")
                            .whereEqualTo("nombre", dispositivo.nombre)
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    firestore.collection("dispositivos").document(document.id).delete()
                                }
                            }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
