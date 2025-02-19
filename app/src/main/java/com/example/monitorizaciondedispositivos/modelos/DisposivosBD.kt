package com.example.monitorizaciondedispositivos.modelos

import kotlinx.serialization.Serializable

@Serializable
sealed class DispositivoBD(val nombre: String)

data class SensorTemperaturaHumedadDB(
    val userid: String = "",
    val nombre: String = "",
    val rangoTemperatura: String = "",
    val rangoHumedad: String = ""
)


data class SensorMovimientoDB(
    val userid: String = "",
    val nombre: String = "",
    val distanciaDeteccion: Int = 0,
    val anguloDeteccion: Int = 0
)

data class SensorAperturaDB(
    val userid: String = "",
    val nombre: String = "",
    val tipoPuerta: String = "",
    val sensibilidad: String = ""
)

data class ReleInteligenteDB(
    val userid: String = "",
    val nombre: String = "",
    val capacidadCorriente: Double = 0.0,
    val voltajeSoportado: Double = 0.0
)

data class ActuadorValvulaDB(
    val userid: String = "",
    val nombre: String = "",
    val tipoValvula: String = "",
    val presionMaxima: Double = 0.0
)

data class ServomotorDB(
    val userid: String = "",
    val nombre: String = "",
    val rangoRotacion: Int = 0,
    val parMaximo: Double = 0.0
)

data class CamaraIPDB(
    val userid: String = "",
    val nombre: String = "",
    val resolucion: String = "",
    val visionNocturna: Boolean = false
)

data class ControladorClimaDB(
    val userid: String = "",
    val nombre: String = "",
    val soporteHVAC: Boolean = false,
    val capacidadBTU: Int = 0
)

data class EstacionMeteorologicaDB(
    val userid: String = "",
    val nombre: String = "",
    val sensores: List<String> = emptyList(),
    val rangoOperacion: String = ""
)