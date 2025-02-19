package com.example.monitorizaciondedispositivos.modelos

import kotlinx.serialization.Serializable

@Serializable
sealed class Dispositivo(val nombre: String)

data class SensorTemperaturaHumedad(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val rangoTemperatura: String,
    val rangoHumedad: String
)

data class SensorMovimiento(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val distanciaDeteccion: Int,
    val anguloDeteccion: Int
)


data class SensorApertura(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val tipoPuerta: String,
    val sensibilidad: String
)


data class ReleInteligente(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val capacidadCorriente: Double,
    val voltajeSoportado: Double
)


data class ActuadorValvula(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val tipoValvula: String,
    val presionMaxima: Double
)


data class Servomotor(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val rangoRotacion: Int,
    val parMaximo: Double
)

data class CamaraIP(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val resolucion: String,
    val visionNocturna: Boolean
)


data class ControladorClima(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val soporteHVAC: Boolean,
    val capacidadBTU: Int
)


data class EstacionMeteorologica(
    val id: String ?= null,
    val userid: String?,
    val nombre: String?,
    val sensores: List<String>,
    val rangoOperacion: String
)
