package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.monitorizaciondedispositivos.modelos.DispositivosBD
import com.example.monitorizaciondedispositivos.modelos.DispositivoBD
import com.example.monitorizaciondedispositivos.modelos.SensorTemperaturaHumedadDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(navController: NavHostController) {
    val dispositivos = remember { mutableStateListOf<DispositivoBD>() }
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        dispositivos.clear()
        dispositivos.addAll(DispositivosBD.obtenerDispositivos()) // Agregar todos los dispositivos

        firestore.collection("dispositivos").addSnapshotListener { snapshot, _ ->
            dispositivos.clear()
            dispositivos.addAll(DispositivosBD.obtenerDispositivos()) // Agregar todos los dispositivos
            snapshot?.documents?.forEach { document ->
                val nombre = document.getString("nombre") ?: "Desconocido"
                dispositivos.add(SensorTemperaturaHumedadDB(nombre = nombre))
            }
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
                    }) { // 🔙 Botón para volver a login
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver a Login")
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
                .background(Color.White) // Fondo blanco
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lista de Dispositivos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(dispositivos) { dispositivo ->
                    DispositivoCard(dispositivo)
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
                is com.example.monitorizaciondedispositivos.modelos.SensorTemperaturaHumedadDB -> {
                    Text("Rango Temperatura: ${dispositivo.rangoTemperatura}")
                    Text("Rango Humedad: ${dispositivo.rangoHumedad}")
                }
                is com.example.monitorizaciondedispositivos.modelos.SensorMovimientoDB -> {
                    Text("Distancia Detección: ${dispositivo.distanciaDeteccion}m")
                    Text("Ángulo Detección: ${dispositivo.anguloDeteccion}°")
                }
                is com.example.monitorizaciondedispositivos.modelos.SensorAperturaDB -> {
                    Text("Tipo de Puerta: ${dispositivo.tipoPuerta}")
                    Text("Sensibilidad: ${dispositivo.sensibilidad}")
                }
                is com.example.monitorizaciondedispositivos.modelos.ReleInteligenteDB -> {
                    Text("Capacidad Corriente: ${dispositivo.capacidadCorriente}A")
                    Text("Voltaje Soportado: ${dispositivo.voltajeSoportado}V")
                }
                is com.example.monitorizaciondedispositivos.modelos.ActuadorValvulaDB -> {
                    Text("Tipo de Válvula: ${dispositivo.tipoValvula}")
                    Text("Presión Máxima: ${dispositivo.presionMaxima} bar")
                }
                is com.example.monitorizaciondedispositivos.modelos.ServomotorDB -> {
                    Text("Rango Rotación: ${dispositivo.rangoRotacion}°")
                    Text("Par Máximo: ${dispositivo.parMaximo} Nm")
                }
                is com.example.monitorizaciondedispositivos.modelos.CamaraIPDB -> {
                    Text("Resolución: ${dispositivo.resolucion}")
                    Text("Visión Nocturna: ${if (dispositivo.visionNocturna) "Sí" else "No"}")
                }
                is com.example.monitorizaciondedispositivos.modelos.ControladorClimaDB -> {
                    Text("Soporta HVAC: ${if (dispositivo.soporteHVAC) "Sí" else "No"}")
                    Text("Capacidad BTU: ${dispositivo.capacidadBTU}")
                }
                is com.example.monitorizaciondedispositivos.modelos.EstacionMeteorologicaDB -> {
                    Text("Sensores: ${dispositivo.sensores.joinToString(", ")}")
                    Text("Rango de Operación: ${dispositivo.rangoOperacion}")
                }
            }
        }
    }
}
