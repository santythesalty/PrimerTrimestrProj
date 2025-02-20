package com.example.monitorizaciondedispositivos.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monitorizaciondedispositivos.data.AuthViewModel
import com.example.monitorizaciondedispositivos.pantallas.*

@Composable
fun Navegacion(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    // Verificamos el estado de autenticación
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        if (authViewModel.isSignedIn()) {
            startDestination = "pantalla_inicio"
        } else {
            startDestination = "login"
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("pantalla_inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onSignInAnonymously = {
                    authViewModel.signInAnonymously()
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate("pantalla_inicio") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        // 🔹 Pantalla Inicio
        composable("pantalla_inicio") {
            PantallaInicio(navController = navController)
        }

        // 🔹 Pantalla para agregar dispositivos (Navegación desde el botón "+")
        composable("pantalla_agregar_dispositivo") {
            PantallaAgregarDispositivo(navController = navController)
        }

        // 🔹 Pantalla de selección de tipo de dispositivo
        composable("seleccion_tipo") {
            PantallaSeleccionTipo { tipo ->
                navController.navigate("pantalla_lista_dispositivos/$tipo")
            }
        }
    }
}
