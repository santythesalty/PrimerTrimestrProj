package com.example.monitorizaciondedispositivos.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.monitorizaciondedispositivos.pantallas.PantallaInicio
import com.example.monitorizaciondedispositivos.pantallas.PantallaSeleccionTipo
import com.example.monitorizaciondedispositivos.pantallas.PantallaListaDispositivos
import com.example.monitorizaciondedispositivos.modelos.*
import com.example.monitorizaciondedispositivos.pantallas.PantallaConfiguracionDispositivo

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Inicio) {
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
