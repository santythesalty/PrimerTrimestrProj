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
import androidx.navigation.toRoute
import com.example.monitorizaciondedispositivos.data.AuthViewModel
import com.example.monitorizaciondedispositivos.pantallas.PantallaInicio
import com.example.monitorizaciondedispositivos.pantallas.PantallaSeleccionTipo
//import com.example.monitorizaciondedispositivos.pantallas.PantallaListaDispositivos
import com.example.monitorizaciondedispositivos.pantallas.PantallaConfiguracionDispositivo
import com.example.monitorizaciondedispositivos.modelos.*
import com.example.monitorizaciondedispositivos.pantallas.LoginScreen
import com.example.monitorizaciondedispositivos.pantallas.SignUpScreen

@Composable
fun Navegacion(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    // Verificamos el estado de autenticación
    var startDestination by remember { mutableStateOf("inicio") }

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
                    navController.navigate("menu") {
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
        // Usamos el objeto Inicio como una ruta. Asegúrate de que el nombre sea correcto.
        composable("inicio") {
            PantallaInicio {
                navController.navigate("seleccion_tipo")
            }
        }
        // Usamos el objeto SeleccionTipo como una ruta
        composable("seleccion_tipo") {
            PantallaSeleccionTipo { tipo ->
                navController.navigate("pantalla_lista_dispositivos/$tipo")
            }
        }
//        // Ruta dinámica para ListaDispositivos con el parámetro 'tipo'
//        composable("pantalla_lista_dispositivos/{tipo}") { backStackEntry ->
//            val tipo = backStackEntry.arguments?.getString("tipo") ?: ""
//            PantallaListaDispositivos(tipo) { dispositivo ->
//                navController.navigate("pantalla_configuracion_dispositivo/$dispositivo")
//            }
//        }
//        // Ruta dinámica para ConfiguracionDispositivo con el parámetro 'dispositivo'
//        composable("pantalla_configuracion_dispositivo/{dispositivo}") { backStackEntry ->
//            val dispositivo = backStackEntry.arguments?.getString("dispositivo") ?: ""
//            PantallaConfiguracionDispositivo(dispositivo)
//        }
    }
}