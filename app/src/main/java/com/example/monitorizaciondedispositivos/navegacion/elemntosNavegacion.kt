package com.example.monitorizaciondedispositivos.navegacion

import kotlinx.serialization.Serializable

@Serializable
object Inicio

@Serializable
object SeleccionTipo

@Serializable
data class ListaDispositivos(val tipo: String)

@Serializable
data class ConfiguracionDispositivo(val dispositivo: String)
