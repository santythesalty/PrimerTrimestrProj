package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.monitorizaciondedispositivos.modelos.SensorTemperaturaHumedadDB

@Composable
fun PantallaInicio(onNavigate: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var dispositivos by remember { mutableStateOf<List<SensorTemperaturaHumedadDB>>(emptyList()) }

    // Obtener datos de Firestore
    LaunchedEffect(Unit) {
        db.collection("dispositivos")
            .get()
            .addOnSuccessListener { result ->
                dispositivos = result.documents.mapNotNull { it.toObject(SensorTemperaturaHumedadDB::class.java) }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Gestión de Dispositivos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar lista de dispositivos
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(dispositivos) { dispositivo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = dispositivo.nombre,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val nuevoDispositivo = SensorTemperaturaHumedadDB(
                userid = "1234",
                nombre = "Nuevo Sensor",
                rangoTemperatura = "20-30°C",
                rangoHumedad = "40-60%"
            )
            db.collection("dispositivos").add(nuevoDispositivo)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar Dispositivo")
        }
    }
}
