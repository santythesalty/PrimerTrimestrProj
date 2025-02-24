package com.example.monitorizaciondedispositivos.mqtt

import android.util.Log
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

object MqttManager {
    private const val BROKER_URL = "tcp://broker.hivemq.com:1883" // Cambia esto si usas otro broker
    private const val CLIENT_ID = "MonitorizacionApp"
    private var client: MqttClient? = null

    fun connect(topics: List<String>, onMessageReceived: (String, String) -> Unit) {
        try {
            client = MqttClient(BROKER_URL, CLIENT_ID, MemoryPersistence())
            val options = MqttConnectOptions()
            options.isCleanSession = true

            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, message: MqttMessage) {
                    val payload = message.toString()
                    Log.d("MQTT", "Mensaje recibido en $topic: $payload")
                    onMessageReceived(topic, payload)
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.e("MQTT", "Conexión perdida: ${cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            client?.connect(options)

            topics.forEach { topic ->
                client?.subscribe(topic)
                Log.d("MQTT", "Suscrito a: $topic")
            }
        } catch (e: Exception) {
            Log.e("MQTT", "Error al conectar: ${e.message}")
        }
    }

    fun publish(topic: String, message: String) {
        try {
            client?.publish(topic, MqttMessage(message.toByteArray()))
        } catch (e: Exception) {
            Log.e("MQTT", "Error al publicar mensaje: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
        } catch (e: Exception) {
            Log.e("MQTT", "Error al desconectar: ${e.message}")
        }
    }
}
