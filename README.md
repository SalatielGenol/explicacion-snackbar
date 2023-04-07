# Snackbar en Compose

En este proyecto se trata de exponer, mostrando una serie de ejemplos, el funcionamiento del elemento Snackbar de Jetpack Compose + Material.

Se ha procurado fundamentar el motivo por el cual para implementar este tipo de elemento en nuestro proyecto se necesita manejar un estado y utilizar una corutina.

La explicación se divide en 5 ejemplos que desgranan lo expuesto en el párrafo anterior, y su código se puede encontrar [aquí](app/src/main/java/es/genol/explicacionSnackbar/Examples.kt)

## Descripción de los ejemplos

### Ejemplo Toast

Este ejemplo se limita a colocar un punto de partida, en base a mostrar un mensaje al usuario de manera muy simple y poco personalizada.

### Ejemplo Snackbar 1 (Estado)

Se explica y demuestra la necesidad de manejar un estado en el uso del elemento

### Ejemplo Snackbar 2 (Corutina)

Se aplica una corutina a dos elementos compose para tratar de entender por qué es necesaria en éste ámbito.

### Ejemplo Snackbar 3 (SnackbarHostState)

Clase que automatiza la presentación en pantalla de las Snackbar. Explicación de su uso.

### Ejemplo Snackbar 4 (Snackbar dentro de Scaffold)

Éste es el ejemplo que recomienda la documentación oficial. Se explican dos variantes:

- Llamada mediante evento
- Activación por cambio de estado

## Notas finales

- Toast no es un elemento de compose, pero se sigue utilizando.
- Existe una clase Snackbar que no pertenece a Jetpack Compose, pero no tendría sentido utilizarla en un proyecto de compose, ya que se estarían mezclando tecnologías sin necesitarlo realmente.
  - Se puede ver una demostración de uso aqui: https://developer.android.com/develop/ui/views/notifications/snackbar/showing
- Las explicaciones se basan en la documentación oficial, y algunas discusiones de stackoverflow:
  - https://developer.android.com/guide/topics/ui/notifiers/toasts
  - https://developer.android.com/jetpack/compose/side-effects
  - https://developer.android.com/reference/kotlin/androidx/compose/material/SnackbarHostState
  - https://stackoverflow.com/questions/68909340/how-to-show-snackbar-with-a-button-onclick-in-jetpack-compose
  - https://m2.material.io/components/snackbars/android


### :warning: Disclamer

Los ejemplos aquí mostrados son fruto del aprendizaje, no son ejemplos de aplicaciones reales, por lo que se han tomado algunas concesiones con la finalidad de que los mismos funcionen correctamente.

Es por ello que se aceptan pull requests, siempre que sean constructivos y no desvirtúen el objetivo del proyecto.
