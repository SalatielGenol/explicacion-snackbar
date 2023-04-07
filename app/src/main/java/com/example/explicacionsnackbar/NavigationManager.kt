package com.example.explicacionsnackbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/* ###############  Elementos necesarios para el funcionamiento de la App ############### */

/* Screen inicial mediante la que se accede a los diferentes ejemplos */

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate(route = Screens.ToastMessage.path) }) {
            Text(text = "Toast Ejemplo")
        }
        Button(onClick = { navController.navigate(route = Screens.SnackMessageNoDissapear.path) }) {
            Text(text = "SnackBar Ejemplo 1")
        }
        Button(onClick = { navController.navigate(route = Screens.SnackMessageWithDelay.path) }) {
            Text(text = "SnackBar Ejemplo 2")
        }
        Button(onClick = { navController.navigate(route = Screens.SnackMessageWithHost.path) }) {
            Text(text = "SnackBar Ejemplo 3")
        }
        Button(onClick = { navController.navigate(route = Screens.SnackMessageWithScaffold.path) }) {
            Text(text = "SnackBar Ejemplo 4")
        }
    }
}

/* Clase que contiene los objetos Screen para el navcontroller */

sealed class Screens(val path: String) {
    object MainScreen : Screens(path = "MainScreen")
    object ToastMessage : Screens(path = "ToastMessage")
    object SnackMessageNoDissapear : Screens(path = "SnackMessageNoDissapear")
    object SnackMessageWithDelay : Screens(path = "SnackMessageWithDelay")
    object SnackMessageWithHost : Screens(path = "SnackMessageWithHost")
    object SnackMessageWithScaffold : Screens(path = "SnackMessageWithScaffold")
}

/* Navigation */

@Composable
fun NavigationManager() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screens.MainScreen.path) {
        composable(route = Screens.MainScreen.path) { MainScreen(navController) }
        composable(route = Screens.ToastMessage.path) { ToastMessage() }
        composable(route = Screens.SnackMessageNoDissapear.path) { SnackMessageNoDissapear() }
        composable(route = Screens.SnackMessageWithDelay.path) { SnackMessagWithDelay() }
        composable(route = Screens.SnackMessageWithHost.path) { SnackMessageWithHost() }
        composable(route = Screens.SnackMessageWithScaffold.path) { SnackMessageWithScaffold() }
    }
}