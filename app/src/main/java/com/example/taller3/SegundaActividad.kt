package com.example.taller3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taller3.ui.theme.Taller3Theme

class SegundaActividad : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller3Theme {
                SegundaPantalla()
            }
        }
    }
}

@Composable
fun SegundaPantalla() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var name by remember { mutableStateOf("") }
    var savedName by remember { mutableStateOf("") }
    val dbHelper = UserDatabaseHelper(context)
    val users = remember { mutableStateListOf<Triple<String, Int, String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var deleteName by remember { mutableStateOf("") }
    var showNotFoundDialog by remember { mutableStateOf(false) }
    var showUserExistsDialog by remember { mutableStateOf(false) }
    var backgroundColor by remember { mutableStateOf(Color(sharedPreferences.getInt("background_color", Color.White.toArgb()))) }

    // Load all users from the database when the composable is first launched
    LaunchedEffect(Unit) {
        users.addAll(dbHelper.getAllUsers())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // TextField to input the user's name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        // Button to save the user's name and background color to the database
        Button(
            onClick = {
                if (dbHelper.userExists(name)) {
                    showUserExistsDialog = true
                } else {
                    dbHelper.insertUser(name, backgroundColor.toArgb())
                    sharedPreferences.edit().putString("user_name", name).apply()
                    savedName = name
                    users.clear()
                    users.addAll(dbHelper.getAllUsers())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Guardar Nombre")
        }
        // Display the saved name
        Text(
            text = "Nombre guardado: $savedName",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        // Button to show the dialog for deleting a user
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Borrar Usuario de SQLite")
        }
        // Button to navigate to AjustesActividad
        Button(
            onClick = {
                context.startActivity(Intent(context, AjustesActividad::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Ir a Ajustes")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Display the list of users saved in SQLite
        Text("Usuarios guardados en SQLite:", modifier = Modifier.padding(bottom = 8.dp))
        users.forEach { (user, color, colorName) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .clickable {
                        name = user
                        backgroundColor = Color(color)
                        sharedPreferences.edit().putInt("background_color", color).apply()
                    }
            ) {
                Text(text = user, modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(24.dp).background(Color(color)))
                Text(text = colorName, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }

    // Dialog to confirm user deletion
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Borrar Usuario") },
            text = {
                Column {
                    Text("Introduce el nombre del usuario a borrar:")
                    TextField(
                        value = deleteName,
                        onValueChange = { deleteName = it }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dbHelper.userExists(deleteName)) {
                            dbHelper.deleteUser(deleteName)
                            users.clear()
                            users.addAll(dbHelper.getAllUsers())
                            showDialog = false
                        } else {
                            showNotFoundDialog = true
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog to show when the user to be deleted is not found
    if (showNotFoundDialog) {
        AlertDialog(
            onDismissRequest = { showNotFoundDialog = false },
            title = { Text("Usuario no encontrado") },
            text = { Text("El usuario con el nombre \"$deleteName\" no fue encontrado.") },
            confirmButton = {
                Button(onClick = { showNotFoundDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Dialog to show when the user already exists
    if (showUserExistsDialog) {
        AlertDialog(
            onDismissRequest = { showUserExistsDialog = false },
            title = { Text("Usuario ya existe") },
            text = { Text("El usuario con el nombre \"$name\" ya existe.") },
            confirmButton = {
                Button(onClick = { showUserExistsDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SegundaPantallaPreview() {
    Taller3Theme {
        SegundaPantalla()
    }
}