//package com.example.monitorizaciondedispositivos.pantallas
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.monitorizaciondedispositivos.modelos.*
//
//@Composable
//fun PantallaListaDispositivos(tipo: String, onNavigate: (Dispositivo) -> Unit) {
//    val dispositivos = when (tipo) {
//        "sensores" -> listOf(
//            SensorTemperaturaHumedad("0°C a 50°C", "10% a 90%"),
//            SensorMovimiento(10, 120),
//            SensorApertura("Puerta", "Alta")
//        )
//        "actuadores" -> listOf(
//            ReleInteligente(10.0, 240.0),
//            ActuadorValvula("Esférica", 16.0),
//            Servomotor(180, 5.0)
//        )
//        "monitoreo" -> listOf(
//            CamaraIP("1080p", true),
//            ControladorClima(true, 12000),
//            EstacionMeteorologica(listOf("Temperatura", "Humedad", "Presión"), "0°C a 50°C")
//        )
//        else -> emptyList()
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
//    ) {
//        Text("Selecciona un $tipo", style = MaterialTheme.typography.titleLarge)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        dispositivos.forEach { dispositivo ->
//            Button(
//                onClick = { onNavigate(dispositivo) },
//                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
//            ) {
//                Text(dispositivo.nombre)
//            }
//        }
//    }
//}
