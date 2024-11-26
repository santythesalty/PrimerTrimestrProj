package com.example.monitorizaciondedispositivos.modelos

import kotlinx.serialization.Serializable

@Serializable
sealed class Dispositivo(val nombre: String)

@Serializable
data class SensorTemperaturaHumedad(
    val rangoTemperatura: String,
    val rangoHumedad: String
) : Dispositivo("Sensor de Temperatura y Humedad")

@Serializable
data class SensorMovimiento(
    val distanciaDeteccion: Int,
    val anguloDeteccion: Int
) : Dispositivo("Sensor de Movimiento (PIR)")

@Serializable
data class SensorApertura(
    val tipoPuerta: String,
    val sensibilidad: String
) : Dispositivo("Sensor de apertura")

@Serializable
data class ReleInteligente(
    val capacidadCorriente: Double,
    val voltajeSoportado: Double
) : Dispositivo("Relé Inteligente")

@Serializable
data class ActuadorValvula(
    val tipoValvula: String,
    val presionMaxima: Double
) : Dispositivo("Actuador de Válvula")

@Serializable
data class Servomotor(
    val rangoRotacion: Int,
    val parMaximo: Double
) : Dispositivo("Servomotor")

@Serializable
data class CamaraIP(
    val resolucion: String,
    val visionNocturna: Boolean
) : Dispositivo("Cámara IP")

@Serializable
data class ControladorClima(
    val soporteHVAC: Boolean,
    val capacidadBTU: Int
) : Dispositivo("Controlador de Clima (HVAC)")

@Serializable
data class EstacionMeteorologica(
    val sensores: List<String>,
    val rangoOperacion: String
) : Dispositivo("Estación Meteorológica")
