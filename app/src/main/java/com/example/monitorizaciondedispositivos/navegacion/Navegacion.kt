package com.example.monitorizaciondedispositivos.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.monitorizaciondedispositivos.data.AuthViewModel
import com.example.monitorizaciondedispositivos.pantallas.*

@Composable
fun Navegacion(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsState(initial = null)
    
    // Determinar la pantalla inicial basada en el estado de autenticación
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("pantalla_inicio") {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"  // Siempre comenzamos en login
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

        // 🔹 Pantalla Inicio (✅ Agregamos authViewModel correctamente)
        composable("pantalla_inicio") {
            PantallaInicio(navController = navController, authViewModel = authViewModel)
        }

        // 🔹 Pantalla para agregar dispositivos (✅ Asegurando la importación y existencia)
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
