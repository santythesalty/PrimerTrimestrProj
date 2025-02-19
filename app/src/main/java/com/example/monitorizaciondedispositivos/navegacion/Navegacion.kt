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
import com.example.monitorizaciondedispositivos.pantallas.PantallaListaDispositivos
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
        composable<Inicio> {
            PantallaInicio {
                navController.navigate(SeleccionTipo)
            }
        }
        composable<SeleccionTipo> {
            PantallaSeleccionTipo { tipo ->
                navController.navigate(ListaDispositivos(tipo))
            }
        }
        composable<ListaDispositivos> { backStackEntry ->
            val data = backStackEntry.toRoute<ListaDispositivos>()
            PantallaListaDispositivos(data.tipo) { dispositivo ->
                navController.navigate(ConfiguracionDispositivo(dispositivo))
            }
        }
        composable<ConfiguracionDispositivo> { backStackEntry ->
            val dispositivo = backStackEntry.toRoute<Dispositivo>()
            PantallaConfiguracionDispositivo(dispositivo)
        }
    }
}
