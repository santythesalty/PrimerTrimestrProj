package com.example.monitorizaciondedispositivos.modelos

import kotlinx.serialization.Serializable

@Serializable
sealed class DispositivoBD(open val nombre: String)

data class SensorTemperaturaHumedadDB(
    override val nombre: String = "",
    val userid: String = "",
    val rangoTemperatura: String = "",
    val rangoHumedad: String = ""
) : DispositivoBD(nombre)

data class SensorMovimientoDB(
    override val nombre: String = "",
    val userid: String = "",
    val distanciaDeteccion: Int = 0,
    val anguloDeteccion: Int = 0
) : DispositivoBD(nombre)

data class SensorAperturaDB(
    override val nombre: String = "",
    val userid: String = "",
    val tipoPuerta: String = "",
    val sensibilidad: String = ""
) : DispositivoBD(nombre)

data class ReleInteligenteDB(
    override val nombre: String = "",
    val userid: String = "",
    val capacidadCorriente: Double = 0.0,
    val voltajeSoportado: Double = 0.0
) : DispositivoBD(nombre)

data class ActuadorValvulaDB(
    override val nombre: String = "",
    val userid: String = "",
    val tipoValvula: String = "",
    val presionMaxima: Double = 0.0
) : DispositivoBD(nombre)

data class ServomotorDB(
    override val nombre: String = "",
    val userid: String = "",
    val rangoRotacion: Int = 0,
    val parMaximo: Double = 0.0
) : DispositivoBD(nombre)

data class CamaraIPDB(
    override val nombre: String = "",
    val userid: String = "",
    val resolucion: String = "",
    val visionNocturna: Boolean = false
) : DispositivoBD(nombre)

data class ControladorClimaDB(
    override val nombre: String = "",
    val userid: String = "",
    val soporteHVAC: Boolean = false,
    val capacidadBTU: Int = 0
) : DispositivoBD(nombre)

data class EstacionMeteorologicaDB(
    override val nombre: String = "",
    val userid: String = "",
    val sensores: List<String> = emptyList(),
    val rangoOperacion: String = ""
) : DispositivoBD(nombre)

object DispositivosBD {
    fun obtenerDispositivos(): List<DispositivoBD> {
        return listOf(
            SensorTemperaturaHumedadDB(nombre = "Sensor de Temperatura y Humedad", rangoTemperatura = "0-50°C", rangoHumedad = "10-90%"),
            SensorMovimientoDB(nombre = "Sensor de Movimiento", distanciaDeteccion = 5, anguloDeteccion = 120),
            SensorAperturaDB(nombre = "Sensor de Apertura", tipoPuerta = "Madera", sensibilidad = "Alta"),
            ReleInteligenteDB(nombre = "Relé Inteligente", capacidadCorriente = 10.0, voltajeSoportado = 220.0),
            ActuadorValvulaDB(nombre = "Actuador de Válvula", tipoValvula = "Esférica", presionMaxima = 5.0),
            ServomotorDB(nombre = "Servomotor", rangoRotacion = 180, parMaximo = 2.5),
            CamaraIPDB(nombre = "Cámara IP", resolucion = "1080p", visionNocturna = true),
            ControladorClimaDB(nombre = "Controlador de Clima", soporteHVAC = true, capacidadBTU = 12000),
            EstacionMeteorologicaDB(nombre = "Estación Meteorológica", sensores = listOf("Temperatura", "Humedad", "Presión"), rangoOperacion = "-10 a 50°C")
        )
    }
}
