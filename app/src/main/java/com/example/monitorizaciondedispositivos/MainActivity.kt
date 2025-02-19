package com.example.monitorizaciondedispositivos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.monitorizaciondedispositivos.data.AuthViewModel
import com.example.monitorizaciondedispositivos.navegacion.Navegacion
import com.example.monitorizaciondedispositivos.ui.theme.MonitorizacionDeDispositivosTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    // Inicializamos el objeto FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase
        FirebaseApp.initializeApp(this)

        // Inicializamos la autenticación de Firebase
        auth = FirebaseAuth.getInstance()

        setContent {
            MonitorizacionDeDispositivosTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()

                    // Inicio de la navegación
                    Navegacion(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }

    // Método para cerrar sesión cuando la actividad se destruye
    override fun onDestroy() {
        super.onDestroy()
        auth.signOut() // Cierra la sesión de Firebase
    }
}
