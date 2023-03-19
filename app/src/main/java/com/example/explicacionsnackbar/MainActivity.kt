package com.example.explicacionsnackbar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explicacionsnackbar.ui.theme.ExplicacionSnackBarTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExplicacionSnackBarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigate()
                }
            }
        }
    }
}

/* ##### EJEMPLO TOAST
Un Toast es un mensaje para dar feedback, de un modo muy simple, al usuario de la app.
Su diseño está predefinido, necesita que la aplicacion esté en primer plano, y no permite más
de dos líneas de mensaje.

Al llamar a la función para mostrar un Toast se le debe pasar el contexto, el mensaje a mostrar
y el tiempo que se quiere mostrar
*/

@Composable
fun ToastMessage() {
    val toastContext = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            Toast.makeText(toastContext, "Mensaje Toast", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Muestra un Toast")
        }
    }
}

/* ##### SNACKBAR EJEMPLO 1
Un Snackbar es un elemento composable que permite dar feedack al usuario, al igual que Toast,
pero mucho mas potente, ya que se puede cambiar su diseño, y se puede ejecutar estando la aplicacion
en segundo plano.

Un Snackbar de por si es un elemento composable sin comportamiento alguno. Tal como Column, o Box.

Por lo que para funcionar como feedback se tienen que usar dos elementos adicionales que realizan
la funcion de mostrar el mensaje:
  - Un SnackbarHostState, es un estadoque controla la cola y el Snackbar que se muestra actualmente
  - Un coroutine, que es una forma de ejecutar una instruccion (o instrucciones) en
  un proceso independiente del proceso principal. En nuestro caso se va a utilizar para que
  las Snackbar se muestren paralelamente al resto de funcionamiento de la aplicacion.

En este primer ejemplo podemos ver como al controlar el valor de un estado mutable, podemos
mostrar o esconder un elemento composable como Snackbar (Realmente podría ser cualquier composable).

Tambien podémos comprobar cómo Snackbar se puede personalizar mediante Modifier, parámetros propios
y añadiendo elementos compose dentro del contenido.
*/

@Composable
fun SnackMessageNoDissapear() {
    /* Declaración de estado mutable booleano que controlará la visualizacion del elemento */
    val visibleSnackMessage = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /* Botones que modifican el valor del estado mutable */
        Button(onClick = { visibleSnackMessage.value = true }) {
            Text(text = "Mostrar SnackBar")
        }

        Button(onClick = { visibleSnackMessage.value = false }) {
            Text(text = "Ocultar SnackBar")
        }
    }

    /*
    Cada vez que el valor del estado mutable cambia, se analiza la condición y se ejecuta o no
    su contenido, que en este caso es la llamada a las funciones que pintan en pantalla el
    Snackbar.
    */
    if (visibleSnackMessage.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Snackbar(
                Modifier.padding(20.dp),
                /*backgroundColor = Color.Green,
                contentColor = Color.Red*/
            ) {
                Row {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Check"
                    )
                    Text(
                        text = "Mensaje SnackBar",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }

    }
}

/* ##### SNACKBAR EJEMPLO 2
El siguiente ejemplo trata de explicar por qué mostrar un mensaje al usuario con compose
necesita una coroutine.

Recordemos que una coroutine es una forma de ejecutar una instruccion de manera independiente
del flujo principal de la aplicacion. En este ejemplo se va a utilizar para automatizar el
reinicio del estado mutable que controla la visualizacion del elemento composable utilizado
para el feedback hacia el usuario.

También se va a aprovechar el ejemplo para demostrar que un elemento Snackbar no es mas que
un composable como otro cualquiera, y que mediante código se puede utilizar cualquier otro
composable como por ejemplo: Text.

NOTA:
  No se ha implementado un sistema de colas que espere a que el mensaje actual termine de
  mostrarse para pintar el siguiente, por lo que los mensajes se van superponiendo uno
  sobre otro.
*/

@Composable
fun SnackMessagWithDelay() {
    /*
    Declaracion de dos estados mutables tipo String nullables. El primero de ellos controlará
    la visualización de un Snackbar y el segundo de un Text.
    */
    val snackMessageText = remember { mutableStateOf<String?>(null) }
    val textMessageText = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /* Botones que modifican el valor de los estados mutables. */
        Button(onClick = {
            snackMessageText.value = "Mensaje Snackbar ${Random.nextInt(1, 100)}"
        }) {
            Text(text = "Mostrar Snackbar")
        }

        Button(onClick = {
            textMessageText.value = "Mensaje Text ${Random.nextInt(1, 100)}"
        }) {
            Text(text = "Mostrar Text")
        }
    }

    /*
    LaunchedEffect, a grandes rasgos, es una función que nos provee de una coroutine que se ejecuta
    en base a un cambio de un estado. Estando ligada al ámbito del compose donde se encuentra, por
    lo que si dicho compose se elimina, la coroutine será cancelada.

    Este comportamiento se puede apreciar cuando se cambia de Snackbar a Text o viceversa: se
    cancela la corotine actual, e inmediatamente se ejecuta una nueva, es por ello que se va
    sumando 1000ms al tiempo que llevase ejecutandose la coroutine anterior.
    */
    LaunchedEffect(key1 = snackMessageText.value, key2 = textMessageText.value) {
        delay(1000)
        snackMessageText.value = null
        textMessageText.value = null
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        /*
        Si el estado de snackMessageText es diferente de null o vacio se pinta el Snackbar
         */
        if (!snackMessageText.value.isNullOrEmpty()) {
            Snackbar(
                Modifier.padding(20.dp),
            ) {
                Text(
                    text = snackMessageText.value!!,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }
        /*
        Si el estado de textMessageText es diferente de null o vacio se pinta el Text
        */
        if (!textMessageText.value.isNullOrEmpty()) {
            Text(
                text = textMessageText.value!!,
                modifier = Modifier
                    .padding(20.dp)
                    .padding(bottom = 50.dp)
                    .background(
                        color = MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(13.dp)
                    .fillMaxWidth()

            )
        }
    }
}

/* ##### SNACKBAR EJEMPLO 3
En este ejemplo se introduce SnackbarHostState, que es una clase que controla la cola y el
Snackbar actual. Mostrandolos en el SnackbarHost, que es una funcion composable que recibe
como parametro el propio SnackbarHostState.

Primero se instancia. Se pasa como argumento al SnackbarHost. Y cuando se quiere mostrar
un mensaje, se llama a la funcion showSnackBar donde se le pueden modificar algunos parametro
a parte del mensaje a mostrar:
    - actionLabel: muestra un boton clickable con un texto predefinido, que cierra el Snackbar
    - duration: define el tiempo que se mostrará en pantalla el Snackbar (Short, Long, Indefinite)

Hay otro cambio. En lugar de usar LaunchEffect, se usa un CoroutineScope. Ya que en lugar de
ejecutarse cuando hay un cambio en un estado, se ejecuta mediante un evento, que es el parametro
onClick del boton, que llama a la funcion .showSnackbar
*/
@Composable
fun SnackMessageWithHost() {
    /*
    Se instancia el SnackbarHostState y se crea la coroutine que se hará cargo
    de ejecutar la funcion que muestre el Snackbar
    */
    val snackBarState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*
        En el evento onClick se lanza la coroutine que llama a la funcion showSnackbar
        del SnackbarHostState que instanciamos arriba. Cuando lo termine de mostrar,
        la coroutine finaliza.
        */
        Button(onClick = {
            coroutineScope.launch {
                snackBarState.showSnackbar(message = "Snackbar temporizada")
            }
        }) {
            Text(text = "Mostrar Snackbar temporizada")
        }
        Button(onClick = {
            /*
            En esta llamada a la funcion showSnackbar se han pasado dos argumentos mas
            ademas del mensaje, uno es el texto que tendrá un boton que incluirá la propia
            snackbar, y que será habilitado solamente cuando reciba un String. Y la duracion,
            que en este caso se le ha especificado que sea indefinida.
            */
            coroutineScope.launch {
                snackBarState.showSnackbar(
                    message = "Snackbar con feedback",
                    actionLabel = "Aceptar",
                    duration = SnackbarDuration.Indefinite
                )
            }
        }) {
            Text(text = "Mostrar Snackbar con feedback")
        }
    }

    /*
    Aqui se delimita la zona donde va a aparecer la Snackbar. Como se puede observar
    ya no es necesario darle margenes ni tamaño, pues SnackbarHost ya tiene esos valores
    por defecto (aunque se podrian modificar).
    */
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(hostState = snackBarState)
    }
}

/* ##### SNACKBAR EJEMPLO 4
Al final llegamos al ejemplo que se recomienda en los codelabs y la documentacion de compose
para el uso de snackbar.

Se trata de introducir el elemento Scaffold, que mediante su parametro scaffoldState controla
el snackbarHostState directamente, sin tener que hacer nada mas.

Tan solo se tiene que instanciar el SnackbarHostState, pasarlo como argumento al Scaffold, y depues
llamar a la funcion showSnackbar desde una coroutine: ya sea mediante evento, o mediante
cambio de estado.
*/
@Composable
fun SnackMessageWithScaffold() {
    /* Se instancia el SnackbarHostState */
    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var onButtonChangeBoolean by remember { mutableStateOf(0) }

    /*
    Este if es necesario ya que al inicializar el estado mutable, LaunchEffect
    lo interpreta como un cambio, y lanza la coroutine.
    */
    if (onButtonChangeBoolean > 0){
        LaunchedEffect(key1 = onButtonChangeBoolean) {
            snackBarState.showSnackbar("Snackbar estado")
        }
    }

    /* Se pasa como argumento al Scaffold el SnackbarHost */
    Scaffold(snackbarHost = { SnackbarHost(snackBarState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* Llamada a la snackbar mediante una corotine asociada a un evento */
            Button(onClick = {
                coroutineScope.launch {
                    snackBarState.showSnackbar("Snackbar evento")
                }
            }) {
                Text(text = "Snackbar mediante evento")
            }
            /* Llamada a la snackbar mediante un cambio de estado */
            Button(onClick = { ++onButtonChangeBoolean }) {
                Text(text = "Snackbar mediante cambio de estado")
            }
        }
    }
}


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
fun Navigate() {
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
