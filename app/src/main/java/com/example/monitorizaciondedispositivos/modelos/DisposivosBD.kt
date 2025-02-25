package com.example.monitorizaciondedispositivos.modelos

import DispositivoBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class DispositivoBD : DispositivoBase {
    @SerialName("nombre_dispositivo")
    abstract override val nombre: String
    
    @SerialName("topic_dispositivo")
    abstract override val topic: String?
    
    @SerialName("estado_dispositivo")
    abstract override val estado: Boolean
}

@Serializable
data class SensorTemperaturaHumedadDB(
    @SerialName("nombre") override val nombre: String = "",
    @SerialName("topic") override val topic: String? = null,
    @SerialName("estado") override val estado: Boolean = false,
    val userid: String = "",
    val rangoTemperatura: String = "",
    val rangoHumedad: String = ""
) : DispositivoBD()

@Serializable
data class SensorMovimientoDB(
    @SerialName("nombre") override val nombre: String = "",
    @SerialName("topic") override val topic: String? = null,
    @SerialName("estado") override val estado: Boolean = false,
    val userid: String = "",
    val distanciaDeteccion: Int = 0,
    val anguloDeteccion: Int = 0
) : DispositivoBD()

data class SensorAperturaDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val tipoPuerta: String = "",
    val sensibilidad: String = ""
) : DispositivoBD()

data class ReleInteligenteDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val capacidadCorriente: Double = 0.0,
    val voltajeSoportado: Double = 0.0
) : DispositivoBD()

data class ActuadorValvulaDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val tipoValvula: String = "",
    val presionMaxima: Double = 0.0
) : DispositivoBD()

data class ServomotorDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val rangoRotacion: Int = 0,
    val parMaximo: Double = 0.0
) : DispositivoBD()

data class CamaraIPDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val resolucion: String = "",
    val visionNocturna: Boolean = false
) : DispositivoBD()

data class ControladorClimaDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val soporteHVAC: Boolean = false,
    val capacidadBTU: Int = 0
) : DispositivoBD()

data class EstacionMeteorologicaDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val sensores: List<String> = emptyList(),
    val rangoOperacion: String = ""
) : DispositivoBD()

data class SensorCalidadAireDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val nivelCO2: Int = 0,
    val nivelVOC: Int = 0,
    val calidadAire: String = ""
) : DispositivoBD()

data class SensorInundacionDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val nivelSensibilidad: String = "",
    val areaCobertura: Double = 0.0,
    val tipoSensor: String = ""
) : DispositivoBD()

data class PanelSolarDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val potenciaMaxima: Int = 0,
    val eficiencia: Double = 0.0,
    val orientacion: String = ""
) : DispositivoBD()

data class SensorPresionDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val rangoPresion: String = "",
    val precision: Double = 0.0,
    val unidadMedida: String = ""
) : DispositivoBD()

data class SensorLuzDB(
    override val nombre: String = "",
    override val topic: String? = null,
    override val estado: Boolean = false,
    val userid: String = "",
    val rangoLuminosidad: String = "",
    val tipoLuz: String = "",
    val sensibilidadEspectral: String = ""
) : DispositivoBD()

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
            EstacionMeteorologicaDB(nombre = "Estación Meteorológica", sensores = listOf("Temperatura", "Humedad", "Presión"), rangoOperacion = "-10 a 50°C"),
            SensorCalidadAireDB(nombre = "Sensor de Calidad del Aire", nivelCO2 = 400, nivelVOC = 100, calidadAire = "Buena"),
            SensorInundacionDB(nombre = "Sensor de Inundación", nivelSensibilidad = "Alta", areaCobertura = 25.0, tipoSensor = "Conductivo"),
            PanelSolarDB(nombre = "Panel Solar", potenciaMaxima = 400, eficiencia = 21.5, orientacion = "Sur"),
            SensorPresionDB(nombre = "Sensor de Presión", rangoPresion = "0-100 bar", precision = 0.1, unidadMedida = "Bar"),
            SensorLuzDB(nombre = "Sensor de Luz", rangoLuminosidad = "0-100000 lux", tipoLuz = "Visible", sensibilidadEspectral = "380-780nm")
        )
    }
}
