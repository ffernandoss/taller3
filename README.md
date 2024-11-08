https://github.com/ffernandoss/taller3.git

taller 3 eventos

en este taller he fusionado el boton de guardar con el de guardar en el SQLite ya que me parecia que hacia lo mismo, y he usado sharedPreferences y SQLite, pero no firebase ya que no sabia muy bien para que implementarlo en el ejercicio ya que todo se guardaba en el SQLite


# Proyecto Taller3

Este proyecto de Android contiene cuatro actividades principales: `MainActivity`, `SegundaActividad`, `AjustesActividad` y `UserDatabaseHelper`. A continuación se explica el funcionamiento de cada una de ellas.

## MainActivity

`MainActivity` es la actividad principal que se muestra al iniciar la aplicación. Su función principal es mostrar un saludo basado en la hora del día y proporcionar un botón para navegar a `SegundaActividad`.

### Métodos

- `onCreate(savedInstanceState: Bundle?)`: Configura la actividad principal, habilita la visualización de borde a borde y establece el contenido utilizando `Taller3Theme`.
- `getGreetingMessage()`: Devuelve un mensaje de saludo basado en la hora actual del día.

### Composables

- `Greeting(name: String, modifier: Modifier = Modifier)`: Muestra un texto de saludo.
- `NavigateButton()`: Proporciona un botón para navegar a `SegundaActividad`.

## SegundaActividad

`SegundaActividad` es una actividad que permite al usuario guardar su nombre y color de fondo en una base de datos SQLite. También permite eliminar usuarios y navegar a `AjustesActividad`.

### Métodos

- `onCreate(savedInstanceState: Bundle?)`: Configura la actividad y establece el contenido utilizando `Taller3Theme`.

### Composables

- `SegundaPantalla()`: Composable principal que contiene la lógica para guardar y eliminar usuarios, así como para navegar a `AjustesActividad`.

## AjustesActividad

`AjustesActividad` es una actividad que permite al usuario seleccionar un color de fondo y guardarlo en `SharedPreferences`.

### Métodos

- `onCreate(savedInstanceState: Bundle?)`: Configura la actividad y establece el contenido utilizando `Taller3Theme`.

### Composables

- `AjustesPantalla()`: Composable principal que permite al usuario seleccionar y guardar un color de fondo, y proporciona un botón para navegar de vuelta a `SegundaActividad`.

## UserDatabaseHelper

`UserDatabaseHelper` es una clase de ayuda para la base de datos que gestiona los datos de los usuarios en una base de datos SQLite.

### Métodos

- `onCreate(db: SQLiteDatabase)`: Crea la tabla de usuarios en la base de datos.
- `onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)`: Actualiza la base de datos eliminando la tabla antigua y creando una nueva.
- `insertUser(name: String, color: Int)`: Inserta un nuevo usuario en la base de datos.
- `updateUserColor(name: String, color: Int)`: Actualiza el color de un usuario existente en la base de datos.
- `getUserColor(name: String): Int`: Obtiene el color de un usuario por su nombre.
- `getAllUsers(): List<Triple<String, Int, String>>`: Obtiene una lista de todos los usuarios en la base de datos.
- `userExists(name: String): Boolean`: Comprueba si un usuario existe en la base de datos.
- `deleteUser(name: String)`: Elimina un usuario de la base de datos.

### Constantes

- `DATABASE_NAME`: Nombre de la base de datos.
- `DATABASE_VERSION`: Versión de la base de datos.
- `TABLE_NAME`: Nombre de la tabla de usuarios.
- `COLUMN_ID`: Columna para el ID del usuario.
- `COLUMN_NAME`: Columna para el nombre del usuario.
- `COLUMN_COLOR`: Columna para el color del usuario.
- `CREATE_TABLE`: Sentencia SQL para crear la tabla de usuarios.
