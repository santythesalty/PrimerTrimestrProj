package com.example.monitorizaciondedispositivos.pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore

// Para los iconos personalizados
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Refresh
import com.example.monitorizaciondedispositivos.mqtt.MqttManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarDispositivo(navController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()

    // Variables de estado para los campos
    var nombre by remember { mutableStateOf("") }
    var tipoDispositivo by remember { mutableStateOf("Selecciona el tipo de dispositivo") }
    var expanded by remember { mutableStateOf(false) }

    // Variables para los atributos según el tipo de dispositivo
    var rangoTemperatura by remember { mutableStateOf("") }
    var rangoHumedad by remember { mutableStateOf("") }
    var distanciaDeteccion by remember { mutableStateOf("") }
    var anguloDeteccion by remember { mutableStateOf("") }
    var tipoPuerta by remember { mutableStateOf("") }
    var sensibilidad by remember { mutableStateOf("") }
    var capacidadCorriente by remember { mutableStateOf("") }
    var voltajeSoportado by remember { mutableStateOf("") }
    var resolucion by remember { mutableStateOf("") }
    var visionNocturna by remember { mutableStateOf(false) }
    var tipoValvula by remember { mutableStateOf("") }
    var presionMaxima by remember { mutableStateOf("") }
    var soporteHVAC by remember { mutableStateOf(false) }
    var capacidadBTU by remember { mutableStateOf("") }
    var sensores by remember { mutableStateOf("") }
    var rangoOperacion by remember { mutableStateOf("") }
    var nivelCO2 by remember { mutableStateOf("") }
    var nivelVOC by remember { mutableStateOf("") }
    var calidadAire by remember { mutableStateOf("") }
    var nivelSensibilidad by remember { mutableStateOf("") }
    var areaCobertura by remember { mutableStateOf("") }
    var tipoSensor by remember { mutableStateOf("") }

    // Variables para Panel Solar
    var potenciaMaxima by remember { mutableStateOf("") }
    var eficiencia by remember { mutableStateOf("") }
    var orientacion by remember { mutableStateOf("") }

    // Variables para Sensor de Presión
    var rangoPresion by remember { mutableStateOf("") }
    var precision by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("") }

    // Variables para Sensor de Luz
    var rangoLuminosidad by remember { mutableStateOf("") }
    var tipoLuz by remember { mutableStateOf("") }
    var sensibilidadEspectral by remember { mutableStateOf("") }

    //Variables del Mqtt
    var estadoDispositivo by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(nombre) {
        if (nombre.isNotEmpty()) {
            MqttManager.connect(listOf(nombre)) { topic, message ->
                if (topic == nombre) {
                    estadoDispositivo = message
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Nuevo Dispositivo",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.shadow(4.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección de tipo de dispositivo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Tipo de Dispositivo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    tipoDispositivo,
                                    fontSize = 16.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Desplegar"
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            listOf(
                                "Sensor de Temperatura y Humedad",
                                "Sensor de Movimiento",
                                "Sensor de Apertura",
                                "Relé Inteligente",
                                "Actuador de Válvula",
                                "Servomotor",
                                "Cámara IP",
                                "Controlador de Clima",
                                "Estación Meteorológica",
                                "Sensor de Calidad del Aire",
                                "Sensor de Inundación",
                                "Panel Solar",
                                "Sensor de Presión",
                                "Sensor de Luz"
                            ).forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        tipoDispositivo = item
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Sección de detalles del dispositivo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Detalles del Dispositivo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del dispositivo") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostrar los campos según el tipo de dispositivo seleccionado
                    when (tipoDispositivo) {
                        "Sensor de Temperatura y Humedad" -> {
                            OutlinedTextField(
                                value = rangoTemperatura,
                                onValueChange = { rangoTemperatura = it },
                                label = { Text("Rango de Temperatura") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Thermostat,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = rangoHumedad,
                                onValueChange = { rangoHumedad = it },
                                label = { Text("Rango Humedad") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Water,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Sensor de Movimiento" -> {
                            OutlinedTextField(
                                value = distanciaDeteccion,
                                onValueChange = { distanciaDeteccion = it },
                                label = { Text("Distancia de Detección (m)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = anguloDeteccion,
                                onValueChange = { anguloDeteccion = it },
                                label = { Text("Ángulo de Detección (°)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Rotate90DegreesCcw,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Sensor de Apertura" -> {
                            OutlinedTextField(
                                value = tipoPuerta,
                                onValueChange = { tipoPuerta = it },
                                label = { Text("Tipo de Puerta") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.DoorFront,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = sensibilidad,
                                onValueChange = { sensibilidad = it },
                                label = { Text("Sensibilidad") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Sensors,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Relé Inteligente" -> {
                            OutlinedTextField(
                                value = capacidadCorriente,
                                onValueChange = { capacidadCorriente = it },
                                label = { Text("Capacidad Corriente (A)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Power,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = voltajeSoportado,
                                onValueChange = { voltajeSoportado = it },
                                label = { Text("Voltaje Soportado (V)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.ElectricBolt,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Actuador de Válvula" -> {
                            OutlinedTextField(
                                value = tipoValvula,
                                onValueChange = { tipoValvula = it },
                                label = { Text("Tipo de Válvula") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = presionMaxima,
                                onValueChange = { presionMaxima = it },
                                label = { Text("Presión Máxima") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Cámara IP" -> {
                            OutlinedTextField(
                                value = resolucion,
                                onValueChange = { resolucion = it },
                                label = { Text("Resolución") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Camera,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Visión Nocturna")
                                Switch(checked = visionNocturna, onCheckedChange = { visionNocturna = it })
                            }
                        }
                        "Controlador de Clima" -> {
                            OutlinedTextField(
                                value = capacidadBTU,
                                onValueChange = { capacidadBTU = it },
                                label = { Text("Capacidad BTU") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.AcUnit,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Soporta HVAC")
                                Switch(checked = soporteHVAC, onCheckedChange = { soporteHVAC = it })
                            }
                        }
                        "Sensor de Calidad del Aire" -> {
                            OutlinedTextField(
                                value = nivelCO2,
                                onValueChange = { nivelCO2 = it },
                                label = { Text("Nivel CO2 (ppm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Air,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = nivelVOC,
                                onValueChange = { nivelVOC = it },
                                label = { Text("Nivel VOC (ppb)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Air,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = calidadAire,
                                onValueChange = { calidadAire = it },
                                label = { Text("Calidad del Aire") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Air,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Sensor de Inundación" -> {
                            OutlinedTextField(
                                value = nivelSensibilidad,
                                onValueChange = { nivelSensibilidad = it },
                                label = { Text("Nivel de Sensibilidad") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Water,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = areaCobertura,
                                onValueChange = { areaCobertura = it },
                                label = { Text("Área de Cobertura (m²)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = tipoSensor,
                                onValueChange = { tipoSensor = it },
                                label = { Text("Tipo de Sensor") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Sensors,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Panel Solar" -> {
                            OutlinedTextField(
                                value = potenciaMaxima,
                                onValueChange = { potenciaMaxima = it },
                                label = { Text("Potencia Máxima (W)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.WbSunny,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = eficiencia,
                                onValueChange = { eficiencia = it },
                                label = { Text("Eficiencia (%)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Build,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = orientacion,
                                onValueChange = { orientacion = it },
                                label = { Text("Orientación") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Sensor de Presión" -> {
                            OutlinedTextField(
                                value = rangoPresion,
                                onValueChange = { rangoPresion = it },
                                label = { Text("Rango de Presión") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = precision,
                                onValueChange = { precision = it },
                                label = { Text("Precisión") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.PrecisionManufacturing,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = unidadMedida,
                                onValueChange = { unidadMedida = it },
                                label = { Text("Unidad de Medida") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Scale,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                        "Sensor de Luz" -> {
                            OutlinedTextField(
                                value = rangoLuminosidad,
                                onValueChange = { rangoLuminosidad = it },
                                label = { Text("Rango de Luminosidad") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.BrightnessHigh,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = tipoLuz,
                                onValueChange = { tipoLuz = it },
                                label = { Text("Tipo de Luz") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Brightness6,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            OutlinedTextField(
                                value = sensibilidadEspectral,
                                onValueChange = { sensibilidadEspectral = it },
                                label = { Text("Sensibilidad Espectral") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.BrightnessLow,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            }



            // Botón de guardar mejorado
            Button(
                onClick = {
                    val dispositivo = HashMap<String, Any>()
                    when (tipoDispositivo) {
                        "Sensor de Temperatura y Humedad" -> {
                            dispositivo["tipo"] = "SensorTemperaturaHumedadDB"
                            dispositivo["nombre"] = nombre
                            dispositivo["rangoTemperatura"] = rangoTemperatura
                            dispositivo["rangoHumedad"] = rangoHumedad
                        }
                        "Sensor de Movimiento" -> {
                            dispositivo["tipo"] = "SensorMovimientoDB"
                            dispositivo["nombre"] = nombre
                            dispositivo["distanciaDeteccion"] = distanciaDeteccion.toIntOrNull() ?: 0
                            dispositivo["anguloDeteccion"] = anguloDeteccion.toIntOrNull() ?: 0
                        }
                        "Sensor de Calidad del Aire" -> {
                            dispositivo["tipo"] = "SensorCalidadAireDB"
                            dispositivo["nombre"] = nombre
                            dispositivo["nivelCO2"] = nivelCO2.toIntOrNull() ?: 0
                            dispositivo["nivelVOC"] = nivelVOC.toIntOrNull() ?: 0
                            dispositivo["calidadAire"] = calidadAire
                        }
                        "Sensor de Inundación" -> {
                            dispositivo["tipo"] = "SensorInundacionDB"
                            dispositivo["nombre"] = nombre
                            dispositivo["nivelSensibilidad"] = nivelSensibilidad
                            dispositivo["areaCobertura"] = areaCobertura.toDoubleOrNull() ?: 0.0
                            dispositivo["tipoSensor"] = tipoSensor
                        }
                        "Panel Solar" -> {
                            dispositivo["tipo"] = "PanelSolarDB"
                            dispositivo["nombre"] = nombre
                            dispositivo["potenciaMaxima"] = potenciaMaxima.toIntOrNull() ?: 0
                            dispositivo["eficiencia"] = eficiencia.toDoubleOrNull() ?: 0.0
                            dispositivo["orientacion"] = orientacion
                        }
                        "Sensor de Presión" -> {
                            dispositivo["tipo"] = "SensorPresionDB"
                            dispositivo["nombre"] = nombre
                            dispositivo["rangoPresion"] = rangoPresion
                            dispositivo["precision"] = precision.toDoubleOrNull() ?: 0.0
                            dispositivo["unidadMedida"] = unidadMedida
                        }
                        "Sensor de Luz" -> {
                            dispositivo["tipo"] = "SensorLuzDB"
                            dispositivo["nombre"] = nombre
                            dispositivo["rangoLuminosidad"] = rangoLuminosidad
                            dispositivo["tipoLuz"] = tipoLuz
                            dispositivo["sensibilidadEspectral"] = sensibilidadEspectral
                        }
                        else -> {
                            dispositivo["tipo"] = "Desconocido"
                            dispositivo["nombre"] = nombre
                        }
                    }
                    
                    firestore.collection("dispositivos").add(dispositivo)
                        .addOnSuccessListener {
                            navController.popBackStack()
                        }
                        .addOnFailureListener { e ->
                            // Manejar el error
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = "Guardar",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Guardar Dispositivo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
