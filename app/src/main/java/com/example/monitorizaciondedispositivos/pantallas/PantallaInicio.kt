package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(navController: NavHostController, authViewModel: AuthViewModel) {
    val dispositivos = remember { mutableStateListOf<DispositivoBD>() }
    val firestore = FirebaseFirestore.getInstance()
    var sinRegistros by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            firestore.collection("dispositivos").addSnapshotListener { snapshot, _ ->
                dispositivos.clear()
                if (snapshot == null || snapshot.isEmpty) {
                    sinRegistros = true
                } else {
                    sinRegistros = false
                    snapshot.documents.forEach { document ->
                        val nombre = document.getString("nombre") ?: "Desconocido"
                        dispositivos.add(SensorTemperaturaHumedadDB(nombre = nombre))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al recuperar dispositivos: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Dispositivos") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("login") {
                            popUpTo("pantalla_inicio") { inclusive = true }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver a Login")
                    }
                },
                actions = { // 🔴 Botón para Cerrar Sesión
                    TextButton(onClick = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("pantalla_inicio") { inclusive = true }
                        }
                    }) {
                        Text("Salir", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
            Text("Lista de Dispositivos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            if (sinRegistros) {
                Text("SIN REGISTROS", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(dispositivos) { dispositivo ->
                        DispositivoCard(dispositivo)
                    }
                }
            }
        }
    }
}

@Composable
fun DispositivoCard(dispositivo: DispositivoBD) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                is SensorAperturaDB -> {
                    Text("Tipo de Puerta: ${dispositivo.tipoPuerta}")
                    Text("Sensibilidad: ${dispositivo.sensibilidad}")
                }
                is ReleInteligenteDB -> {
                    Text("Capacidad Corriente: ${dispositivo.capacidadCorriente}A")
                    Text("Voltaje Soportado: ${dispositivo.voltajeSoportado}V")
                }
                is ActuadorValvulaDB -> {
                    Text("Tipo de Válvula: ${dispositivo.tipoValvula}")
                    Text("Presión Máxima: ${dispositivo.presionMaxima} bar")
                }
                is ServomotorDB -> {
                    Text("Rango Rotación: ${dispositivo.rangoRotacion}°")
                    Text("Par Máximo: ${dispositivo.parMaximo} Nm")
                }
                is CamaraIPDB -> {
                    Text("Resolución: ${dispositivo.resolucion}")
                    Text("Visión Nocturna: ${if (dispositivo.visionNocturna) "Sí" else "No"}")
                }
                is ControladorClimaDB -> {
                    Text("Soporta HVAC: ${if (dispositivo.soporteHVAC) "Sí" else "No"}")
                    Text("Capacidad BTU: ${dispositivo.capacidadBTU}")
                }
                is EstacionMeteorologicaDB -> {
                    Text("Sensores: ${dispositivo.sensores.joinToString(", ")}")
                    Text("Rango de Operación: ${dispositivo.rangoOperacion}")
                }
            }
        }
    }
}
