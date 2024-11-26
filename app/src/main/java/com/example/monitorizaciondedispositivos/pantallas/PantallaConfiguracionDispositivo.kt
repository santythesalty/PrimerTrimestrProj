package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monitorizaciondedispositivos.modelos.ActuadorValvula
import com.example.monitorizaciondedispositivos.modelos.CamaraIP
import com.example.monitorizaciondedispositivos.modelos.ControladorClima
import com.example.monitorizaciondedispositivos.modelos.Dispositivo
import com.example.monitorizaciondedispositivos.modelos.EstacionMeteorologica
import com.example.monitorizaciondedispositivos.modelos.ReleInteligente
import com.example.monitorizaciondedispositivos.modelos.SensorApertura
import com.example.monitorizaciondedispositivos.modelos.SensorMovimiento
import com.example.monitorizaciondedispositivos.modelos.SensorTemperaturaHumedad
import com.example.monitorizaciondedispositivos.modelos.Servomotor

@Composable
fun PantallaConfiguracionDispositivo(dispositivo: Dispositivo) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Configuración de ${dispositivo.nombre}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (dispositivo) {
            is SensorTemperaturaHumedad -> {
                Text("Rango de Temperatura: ${dispositivo.rangoTemperatura}")
                Text("Rango de Humedad: ${dispositivo.rangoHumedad}")
            }
            is SensorMovimiento -> {
                Text("Distancia de Detección: ${dispositivo.distanciaDeteccion} metros")
                Text("Ángulo de Detección: ${dispositivo.anguloDeteccion} grados")
            }
            is SensorApertura -> {
                Text("Tipo de Puerta: ${dispositivo.tipoPuerta}")
                Text("Sensibilidad: ${dispositivo.sensibilidad}")
            }
            is ReleInteligente -> {
                Text("Capacidad de Corriente: ${dispositivo.capacidadCorriente} A")
                Text("Voltaje Soportado: ${dispositivo.voltajeSoportado} V")
            }
            is ActuadorValvula -> {
                Text("Tipo de Válvula: ${dispositivo.tipoValvula}")
                Text("Presión Máxima: ${dispositivo.presionMaxima} bar")
            }
            is Servomotor -> {
                Text("Rango de Rotación: ${dispositivo.rangoRotacion} grados")
                Text("Par Máximo: ${dispositivo.parMaximo} Nm")
            }
            is CamaraIP -> {
                Text("Resolución: ${dispositivo.resolucion}")
                Text("Visión Nocturna: ${if (dispositivo.visionNocturna) "Sí" else "No"}")
            }
            is ControladorClima -> {
                Text("Soporte HVAC: ${if (dispositivo.soporteHVAC) "Sí" else "No"}")
                Text("Capacidad BTU: ${dispositivo.capacidadBTU}")
            }
            is EstacionMeteorologica -> {
                Text("Sensores Disponibles: ${dispositivo.sensores.joinToString(", ")}")
                Text("Rango de Operación: ${dispositivo.rangoOperacion}")
            }
        }
    }
}
