package com.example.monitorizaciondedispositivos.mqtt

import android.util.Log
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import kotlinx.coroutines.*

object MqttManager {
    private const val BROKER_URL = "tcp://broker.hivemq.com:1883"
    private val CLIENT_ID = "MonitorizacionApp${System.currentTimeMillis()}"
    private var client: MqttClient? = null
    private var currentTopics = mutableSetOf<String>()

    fun connect(topics: List<String>, onMessageReceived: (String, String) -> Unit) {
        try {
            disconnect()
            
            client = MqttClient(BROKER_URL, CLIENT_ID, MemoryPersistence())
            val options = MqttConnectOptions().apply {
                isCleanSession = true
                connectionTimeout = 30
                keepAliveInterval = 60
                isAutomaticReconnect = true
            }

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
            currentTopics.clear()
            currentTopics.addAll(topics)
            
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
            Log.d("MQTT", "Mensaje publicado en topic: $topic - mensaje: $message")
        } catch (e: Exception) {
            Log.e("MQTT", "Error al publicar mensaje: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            if (client?.isConnected == true) {
                currentTopics.forEach { topic ->
                    client?.unsubscribe(topic)
                }
                client?.disconnect()
            }
            client = null
            currentTopics.clear()
        } catch (e: Exception) {
            Log.e("MQTT", "Error al desconectar: ${e.message}")
        }
    }
}
